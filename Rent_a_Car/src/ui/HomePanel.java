package ui;

import java.awt.*;
import javax.swing.*;

public class HomePanel extends JPanel {
    private final MainFrame mainFrame;

    public HomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 상단 로고
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel logoLabel = new JLabel("Rent Car");
        logoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        logoLabel.setForeground(new Color(13, 27, 62));

        JButton exitButton = new JButton("⏻");
        exitButton.setBorderPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.addActionListener(e -> System.exit(0));

        topPanel.add(logoLabel, BorderLayout.WEST);
        topPanel.add(exitButton, BorderLayout.EAST);

        // 중앙 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // 차량 검색 버튼
        JButton searchButton = new JButton("차량 검색  RENT A VEHICLE");
        searchButton.setBackground(new Color(13, 27, 62));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        searchButton.setPreferredSize(new Dimension(350, 80));
        searchButton.setMaximumSize(new Dimension(350, 80));
        searchButton.setBorderPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> mainFrame.showPanel("SEARCH"));

        // Active Bookings / Past Rentals 버튼
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        middlePanel.setBackground(Color.WHITE);
        middlePanel.setMaximumSize(new Dimension(350, 120));

        JButton bookingButton = new JButton(
            "<html><center>Active Bookings<br>예약/취소/반납</center></html>"
        );
        bookingButton.setBackground(new Color(235, 240, 255));
        bookingButton.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        bookingButton.setBorderPainted(false);
        bookingButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookingButton.addActionListener(e -> mainFrame.showPanel("BOOKING"));

        JButton historyButton = new JButton(
            "<html><center>Past Rentals<br>대여 이력 조회</center></html>"
        );
        historyButton.setBackground(new Color(235, 240, 255));
        historyButton.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        historyButton.setBorderPainted(false);
        historyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        historyButton.addActionListener(e -> mainFrame.showPanel("HISTORY"));

        middlePanel.add(bookingButton);
        middlePanel.add(historyButton);

        // 관리자 버튼
        JButton adminButton = new JButton("관리자 기능  Fleet & User Management  >");
        adminButton.setBackground(new Color(245, 245, 245));
        adminButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        adminButton.setMaximumSize(new Dimension(350, 60));
        adminButton.setBorderPainted(false);
        adminButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminButton.addActionListener(e -> mainFrame.showPanel("ADMIN"));

        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(searchButton);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(middlePanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(adminButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}