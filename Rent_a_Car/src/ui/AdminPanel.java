package ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Car;
import model.Spot;
import spot_management.SpotService;

public class AdminPanel extends JPanel {
    private final SpotService service;
    private final MainFrame mainFrame;
    private DefaultTableModel spotTableModel;
    private DefaultTableModel carTableModel;

    public AdminPanel(SpotService service, MainFrame mainFrame) {
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

        // 탭 (지점 관리 / 차량 관리)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("지점 관리", createSpotPanel());
        tabbedPane.addTab("차량 관리", createCarPanel());

        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    // 지점 관리 패널
    private JPanel createSpotPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // 지점 목록 테이블
        String[] spotColumns = {"지점번호", "지점명", "지점위치"};
        spotTableModel = new DefaultTableModel(spotColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable spotTable = new JTable(spotTableModel);
        spotTable.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        spotTable.setRowHeight(30);
        spotTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 13));
        spotTable.getTableHeader().setBackground(new Color(235, 240, 255));

        JScrollPane spotScrollPane = new JScrollPane(spotTable);

        // 지점 목록 조회 버튼
        JButton loadSpotButton = new JButton("지점 목록 조회");
        loadSpotButton.setBackground(new Color(13, 27, 62));
        loadSpotButton.setForeground(Color.WHITE);
        loadSpotButton.setBorderPainted(false);
        loadSpotButton.addActionListener(e -> {
            spotTableModel.setRowCount(0);
            List<Spot> spotList = service.getAllSpots();
            if(spotList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "지점이 없습니다.");
                return;
            }
            for(Spot spot : spotList) {
                spotTableModel.addRow(new Object[]{
                    spot.getSpot_no(),
                    spot.getSpot_name(),
                    spot.getSpot_location()
                });
            }
        });

        panel.add(loadSpotButton, BorderLayout.NORTH);
        panel.add(spotScrollPane, BorderLayout.CENTER);

        return panel;
    }

    // 차량 관리 패널
    private JPanel createCarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // 입력 영역
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBackground(Color.WHITE);

        JLabel spotLabel = new JLabel("지점 번호 입력: ");
        spotLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        JTextField spotNoField = new JTextField(5);

        JButton searchButton = new JButton("조회");
        searchButton.setBackground(new Color(13, 27, 62));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        inputPanel.add(spotLabel);
        inputPanel.add(spotNoField);
        inputPanel.add(searchButton);

        // 차량 목록 테이블
        String[] carColumns = {"차량번호", "차종", "상태", "일일요금"};
        carTableModel = new DefaultTableModel(carColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable carTable = new JTable(carTableModel);
        carTable.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        carTable.setRowHeight(30);
        carTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 13));
        carTable.getTableHeader().setBackground(new Color(235, 240, 255));

        JScrollPane carScrollPane = new JScrollPane(carTable);

        // 버튼 클릭 이벤트
        searchButton.addActionListener(e -> {
            try {
                int spotNo = Integer.parseInt(spotNoField.getText());
                List<Car> carList = service.getCarsBySpot(spotNo);

                carTableModel.setRowCount(0);

                if(carList.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "해당 지점에 보유 중인 차량이 없습니다.");
                    return;
                }

                for(Car car : carList) {
                    String state = car.isRental_availabiliy() ? "대여 가능" : "대여 중";
                    carTableModel.addRow(new Object[]{
                        car.getCar_no(),
                        car.getCar_type(),
                        state,
                        car.getDaily_rental_fee() + "원"
                    });
                }

            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "지점번호는 숫자만 입력 가능합니다.");
            }
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(carScrollPane, BorderLayout.CENTER);

        return panel;
    }
}