package ui;

import java.awt.*;
import javax.swing.*;
import search.CarService;
import reservation.ReservationMenuUI;
import Rental_record.RentalService;
import spot_management.SpotService;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // 각 Panel
    private HomePanel homePanel;
    private SearchPanel searchPanel;
    private BookingPanel bookingPanel;
    private HistoryPanel historyPanel;
    private AdminPanel adminPanel;

    public MainFrame(
        CarService carService,
        ReservationMenuUI reservationMenuUI,
        RentalService rentalService,
        SpotService spotService
    ) {
        // 창 기본 설정
        setTitle("Rent Car");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // CardLayout 설정
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 각 Panel 생성
        homePanel = new HomePanel(this);
        searchPanel = new SearchPanel(carService, this);
        bookingPanel = new BookingPanel(this);
        historyPanel = new HistoryPanel(rentalService, this);
        adminPanel = new AdminPanel(spotService, this);

        // Panel 등록
        mainPanel.add(homePanel, "HOME");
        mainPanel.add(searchPanel, "SEARCH");
        mainPanel.add(bookingPanel, "BOOKING");
        mainPanel.add(historyPanel, "HISTORY");
        mainPanel.add(adminPanel, "ADMIN");

        add(mainPanel);
        setVisible(true);
    }

    // 화면 전환 메서드
    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }
}