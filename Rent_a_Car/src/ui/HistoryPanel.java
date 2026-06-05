package ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Rental_record.RecordDTO;
import Rental_record.RentalService;

public class HistoryPanel extends JPanel {
    private final RentalService service;
    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;

    public HistoryPanel(RentalService service, MainFrame mainFrame) {
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

        // 중앙 - 입력 + 테이블
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // 입력 영역
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBackground(Color.WHITE);

        JLabel idLabel = new JLabel("Enter User ID: ");
        idLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        JTextField userIdField = new JTextField(10);

        JButton searchButton = new JButton("조회");
        searchButton.setBackground(new Color(13, 27, 62));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        inputPanel.add(idLabel);
        inputPanel.add(userIdField);
        inputPanel.add(searchButton);

        // 결과 테이블
        String[] columns = {
            "Rental ID", "Car Number",
            "Rental Date", "Expected Return",
            "Actual Return", "State", "Total Fee"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // 테이블 수정 불가
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(235, 240, 255));

        JScrollPane scrollPane = new JScrollPane(table);

        // 버튼 클릭 이벤트
        searchButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                List<RecordDTO> list = service.getRentalRecord(userId);

                tableModel.setRowCount(0);

                if(list.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "There is no Rental Record.");
                    return;
                }

                for(RecordDTO dto : list) {
                    tableModel.addRow(new Object[]{
                        dto.getRental_id(),
                        dto.getCar_no(),
                        dto.getRental_date(),
                        dto.getExpected_return_date(),
                        dto.getActual_return_date() != null
                            ? dto.getActual_return_date() : "-",
                        dto.getRental_state(),
                        dto.getTotal_fee() + "원"
                    });
                }

            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "User ID는 숫자만 입력 가능합니다.");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "오류가 발생했습니다.");
                ex.printStackTrace();
            }
        });

        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}