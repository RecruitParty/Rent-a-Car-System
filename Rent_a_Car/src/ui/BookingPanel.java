package ui;

import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import db.DBConnector;
import reservation.ReservationService;
import reservation.reservationCancelService;
import reservation.returnCarService;

public class BookingPanel extends JPanel {
    private final MainFrame mainFrame;
    
    private JTextField createDateField() {
        JTextField field = new JTextField("YYYY-MM-DD");
        field.setForeground(Color.GRAY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if(field.getText().equals("YYYY-MM-DD")) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if(field.getText().isEmpty()) {
                    field.setText("YYYY-MM-DD");
                    field.setForeground(Color.GRAY);
                }
            }
        });
        return field;
    }

    public BookingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Rent Car");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setForeground(new Color(13, 27, 62));

        JButton backButton = new JButton("뒤로");
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(e -> mainFrame.showPanel("HOME"));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(backButton, BorderLayout.EAST);

        // 탭 (New Booking / Cancel / Return)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("New Booking", createNewBookingPanel());
        tabbedPane.addTab("Cancel", createCancelPanel());
        tabbedPane.addTab("Return", createReturnPanel());

        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    // 예약 패널
    private JPanel createNewBookingPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JTextField cusIDField = new JTextField();
        JTextField carNoField = new JTextField();
        JTextField startLocationField = new JTextField();
        JTextField endLocationField = new JTextField();
        JTextField startDateField = createDateField();
        JTextField dueDateField = createDateField();

        JButton bookButton = new JButton("예약하기");
        bookButton.setBackground(new Color(13, 27, 62));
        bookButton.setForeground(Color.WHITE);
        bookButton.setBorderPainted(false);
        bookButton.addActionListener(e -> {
            try {
                // 고객 ID 검증
                int cusID;
                try {
                    cusID = Integer.parseInt(cusIDField.getText());
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "고객 ID는 숫자만 입력 가능합니다.");
                    return;
                }

                // 고객 존재 여부 확인
                try(Connection conn = DBConnector.getConnection();
                    PreparedStatement userStmt = conn.prepareStatement(
                        "SELECT * FROM customer WHERE user_id = ?")) {
                    userStmt.setInt(1, cusID);
                    ResultSet userRs = userStmt.executeQuery();
                    if(!userRs.next()) {
                        JOptionPane.showMessageDialog(this, "등록되지 않는 아이디입니다.");
                        return;
                    }
                } catch(SQLException ex) {
                    ex.printStackTrace();
                    return;
                }

                // 차량 존재 및 대여 가능 여부 확인
                String carNo = carNoField.getText();
                try(Connection conn = DBConnector.getConnection();
                    PreparedStatement checkStmt = conn.prepareStatement(
                        "SELECT rental_availability FROM car WHERE car_no = ?")) {
                    checkStmt.setString(1, carNo);
                    ResultSet rs = checkStmt.executeQuery();
                    if(!rs.next()) {
                        JOptionPane.showMessageDialog(this, "존재하지 않는 차량입니다.");
                        return;
                    }
                    if(!rs.getBoolean("rental_availability")) {
                        JOptionPane.showMessageDialog(this, "이미 대여중인 차량입니다.");
                        return;
                    }
                } catch(SQLException ex) {
                    ex.printStackTrace();
                    return;
                }

                // 위치 검증
                int startLocation;
                int endLocation;
                try {
                    startLocation = Integer.parseInt(startLocationField.getText());
                    endLocation = Integer.parseInt(endLocationField.getText());
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "위치는 숫자만 입력 가능합니다.");
                    return;
                }

                // 날짜 검증
                Date startDate;
                Date dueDate;
                try {
                    startDate = Date.valueOf(startDateField.getText());
                    dueDate = Date.valueOf(dueDateField.getText());
                } catch(IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "날짜 형식을 확인해주세요. (YYYY-MM-DD)");
                    return;
                }

                boolean result = ReservationService.reservation(
                	    cusID, carNo, startLocation, endLocation, startDate, dueDate
                	);
                if(result) {
                    JOptionPane.showMessageDialog(this, "예약이 완료되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(this, "예약에 실패했습니다.");
                }

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "오류가 발생했습니다.");
                ex.printStackTrace();
            }
        });

        panel.add(new JLabel("고객 ID")); panel.add(cusIDField);
        panel.add(new JLabel("차량 번호")); panel.add(carNoField);
        panel.add(new JLabel("대여 위치")); panel.add(startLocationField);
        panel.add(new JLabel("반납 위치")); panel.add(endLocationField);
        panel.add(new JLabel("대여 날짜")); panel.add(startDateField);
        panel.add(new JLabel("반납 예정 날짜")); panel.add(dueDateField);
        panel.add(new JLabel("")); panel.add(bookButton);

        return panel;
    }

    // 예약 취소 패널
    private JPanel createCancelPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JTextField cusIDField = new JTextField();
        JTextField carNoField = new JTextField();

        JButton cancelButton = new JButton("예약 취소");
        cancelButton.setBackground(new Color(13, 27, 62));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorderPainted(false);
        cancelButton.addActionListener(e -> {
            try {
                int cusID;
                try {
                    cusID = Integer.parseInt(cusIDField.getText());
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "고객 ID는 숫자만 입력 가능합니다.");
                    return;
                }
                String carNo = carNoField.getText();
                boolean result = reservationCancelService.reservationCancel(cusID, carNo);
                if(result) {
                	JOptionPane.showMessageDialog(this, "예약이 취소되었습니다.");
                }else {
                	JOptionPane.showMessageDialog(this, "오류가 발생했습니다. 나중에 다시 시도해주세요.");
                }
                
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "오류가 발생했습니다.");
                ex.printStackTrace();
            }
        });

        panel.add(new JLabel("고객 ID")); panel.add(cusIDField);
        panel.add(new JLabel("차량 번호")); panel.add(carNoField);
        panel.add(new JLabel("")); panel.add(cancelButton);

        return panel;
    }

    // 반납 패널
    private JPanel createReturnPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JTextField cusIDField = new JTextField();
        JTextField carNoField = new JTextField();
        JTextField returnDestField = new JTextField();
        JTextField returnDateField = createDateField();

        JButton returnButton = new JButton("반납하기");
        returnButton.setBackground(new Color(13, 27, 62));
        returnButton.setForeground(Color.WHITE);
        returnButton.setBorderPainted(false);
        returnButton.addActionListener(e -> {
            try {
                // 고객 ID 검증
                int cusID;
                try {
                    cusID = Integer.parseInt(cusIDField.getText());
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "고객 ID는 숫자만 입력 가능합니다.");
                    return;
                }

                // 고객 존재 여부 확인
                try(Connection conn = DBConnector.getConnection();
                    PreparedStatement userStmt = conn.prepareStatement(
                        "SELECT * FROM customer WHERE user_id = ?")) {
                    userStmt.setInt(1, cusID);
                    ResultSet userRs = userStmt.executeQuery();
                    if(!userRs.next()) {
                        JOptionPane.showMessageDialog(this, "등록되지 않는 아이디입니다.");
                        return;
                    }
                } catch(SQLException ex) {
                    ex.printStackTrace();
                    return;
                }

                // 차량 존재 및 대여 가능 여부 확인
                String carNo = carNoField.getText();
                try(Connection conn = DBConnector.getConnection();
                    PreparedStatement checkStmt = conn.prepareStatement(
                        "SELECT rental_availability FROM car WHERE car_no = ?")) {
                    checkStmt.setString(1, carNo);
                    ResultSet rs = checkStmt.executeQuery();
                    if(!rs.next()) {
                        JOptionPane.showMessageDialog(this, "존재하지 않는 차량입니다.");
                        return;
                    }
                    if(rs.getBoolean("rental_availability")) {
                        JOptionPane.showMessageDialog(this, "이미 대여중인 차량입니다.");
                        return;
                    }
                } catch(SQLException ex) {
                    ex.printStackTrace();
                    return;
                }

                // 위치, 날짜 검증
                int returnDest;
                try {
                    returnDest = Integer.parseInt(returnDestField.getText());
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "위치는 숫자만 입력 가능합니다.");
                    return;
                }

                Date returnDate;
                try {
                    returnDate = Date.valueOf(returnDateField.getText());
                } catch(IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "날짜 형식을 확인해주세요. (YYYY-MM-DD)");
                    return;
                }

                boolean result = returnCarService.returnCar(cusID, carNo, returnDest, returnDate);
                if(result) {
                	JOptionPane.showMessageDialog(this, "반납이 완료되었습니다.");
                }else {
                	JOptionPane.showMessageDialog(this, "오류가 발생했습니다. 나중에 다시 시도해주세요.");
                }

            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "오류가 발생했습니다.");
                ex.printStackTrace();
            }
        });

        panel.add(new JLabel("고객 ID")); panel.add(cusIDField);
        panel.add(new JLabel("차량 번호")); panel.add(carNoField);
        panel.add(new JLabel("반납 위치")); panel.add(returnDestField);
        panel.add(new JLabel("반납 날짜")); panel.add(returnDateField);
        panel.add(new JLabel("")); panel.add(returnButton);

        return panel;
    }
}