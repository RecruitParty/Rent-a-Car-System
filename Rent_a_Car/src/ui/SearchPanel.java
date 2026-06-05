package ui;

import java.awt.*;
import java.sql.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Car;
import model.Spot;
import search.CarService;

public class SearchPanel extends JPanel {
    private final CarService service;
    private final MainFrame mainFrame;
    private DefaultTableModel availableModel;
    
    // 차량 검색 결과 테이블
    private DefaultTableModel carTableModel;
    private JTable carTable;
    
    // 지점 목록 테이블
    private DefaultTableModel spotTableModel;
    private JTable spotTable;
    
    // 대여 가능 여부 확인
    private JComboBox<String> carTypeCombo;
    private JTextField rentalDateField;
    private JTextField returnDateField;
    
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

    public SearchPanel(CarService service, MainFrame mainFrame) {
        this.service = service;
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

        // 중앙 - 탭으로 구분
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);

        // 탭 1 - 차종 검색
        tabbedPane.addTab("차종 검색", createTypeSearchPanel());

        // 탭 2 - 지점 목록
        tabbedPane.addTab("지점 목록", createSpotPanel());

        // 탭 3 - 대여 가능 여부
        tabbedPane.addTab("대여 가능 확인", createAvailabilityPanel());

        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    // 차종 검색 패널
    private JPanel createTypeSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // 차종 버튼들 (피그마 2번 화면)
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        buttonPanel.setBackground(new Color(13, 27, 62));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] carTypes = {"SUV", "세단", "승합차", "버스", "전기차", "경차", "중형차", "RV"};
        for(String type : carTypes) {
            JButton btn = new JButton(type);
            btn.setBackground(new Color(50, 65, 100));
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> searchByType(type));
            buttonPanel.add(btn);
        }

        // 결과 테이블
        String[] columns = {"차량번호", "차종", "지점번호", "일일요금"};
        carTableModel = new DefaultTableModel(columns, 0);
        carTable = new JTable(carTableModel);
        JScrollPane scrollPane = new JScrollPane(carTable);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // 지점 목록 패널
    private JPanel createSpotPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        String[] columns = {"지점번호", "지점명", "지점위치"};
        spotTableModel = new DefaultTableModel(columns, 0);
        spotTable = new JTable(spotTableModel);
        JScrollPane scrollPane = new JScrollPane(spotTable);

        // 지점 목록 바로 로드
        JButton loadButton = new JButton("지점 목록 조회");
        loadButton.setBackground(new Color(13, 27, 62));
        loadButton.setForeground(Color.WHITE);
        loadButton.setBorderPainted(false);
        loadButton.addActionListener(e -> loadSpots());

        panel.add(loadButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // 대여 가능 여부 패널
    private JPanel createAvailabilityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        String[] carTypes = {"SUV", "세단", "승합차", "버스", "전기차", "경차", "중형차", "RV"};
        carTypeCombo = new JComboBox<>(carTypes);
        rentalDateField = createDateField();
        returnDateField = createDateField();

        JButton checkButton = new JButton("CHECK AVAILABILITY");
        checkButton.setBackground(new Color(13, 27, 62));
        checkButton.setForeground(Color.WHITE);
        checkButton.setBorderPainted(false);
        checkButton.addActionListener(e -> checkAvailability());

        inputPanel.add(new JLabel("차종 선택"));
        inputPanel.add(carTypeCombo);
        inputPanel.add(new JLabel("대여 날짜"));
        inputPanel.add(rentalDateField);
        inputPanel.add(new JLabel("반납 날짜"));
        inputPanel.add(returnDateField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(checkButton);

        // 결과 테이블
        String[] columns = {"차량번호", "차종", "지점번호", "일일요금"};
        availableModel = new DefaultTableModel(columns, 0);
        JTable availableTable = new JTable(availableModel);
        JScrollPane scrollPane = new JScrollPane(availableTable);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // 차종으로 검색
    private void searchByType(String type) {
        carTableModel.setRowCount(0);
        List<Car> carList = service.searchByType(type);
        for(Car car : carList) {
            carTableModel.addRow(new Object[]{
                car.getCar_no(),
                car.getCar_type(),
                car.getSpot_no(),
                car.getDaily_rental_fee() + "원"
            });
        }
    }

    // 지점 목록 로드
    private void loadSpots() {
        spotTableModel.setRowCount(0);
        List<model.Spot> spotList = service.getAllSpots();
        for(model.Spot spot : spotList) {
            spotTableModel.addRow(new Object[]{
                spot.getSpot_no(),
                spot.getSpot_name(),
                spot.getSpot_location()
            });
        }
    }

    // 대여 가능 여부 확인
    private void checkAvailability() {
        try {
            String carType = (String) carTypeCombo.getSelectedItem();
            Date rentalDate = Date.valueOf(rentalDateField.getText());
            Date returnDate = Date.valueOf(returnDateField.getText());

            List<Car> availableCars = service.checkRentalAvailability(carType, rentalDate, returnDate);
            availableModel.setRowCount(0);

            if(availableCars.isEmpty()) {
                JOptionPane.showMessageDialog(this, "예약 가능한 차량이 없습니다.");
            }else {
            	for(Car car : availableCars) {
                    availableModel.addRow(new Object[]{
                        car.getCar_no(),
                        car.getCar_type(),
                        car.getSpot_no(),
                        car.getDaily_rental_fee() + "원"
                    });
            	}
            }
        } catch(IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "날짜 형식과 일자를 확인해주세요. (YYYY-MM-DD)");
        }
    }
}