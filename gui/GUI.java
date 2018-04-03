package gui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import api.Restaurant;
import api.APIRequest;
import application.Application;

public class GUI {
    private Application app;
    private JFrame frame;
    private JScrollPane searchPane;
    private JScrollPane favoritesPane;
    private JPanel searchPanel;
    private JPanel favoritesPanel;
    private JPanel topBar;
    private JPanel bottomBar;
    private JLabel userLabel;
    private JLabel cacheLabel;
    private JLabel statusLabel;
    private JTabbedPane tabbedPane;

    public static final int WIDTH = 1050;
    public static final int HEIGHT = 700;
    public static final int SCROLL_AMOUNT = 10;

    public GUI(Application app, String userName) {
        this.frame = new JFrame("LA Foodie");
        this.frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new BorderLayout());

        this.userLabel = new JLabel("Welcome, " + userName + "!");
        this.app = app;
    }

    public void init() {
        createTopBar();
        createTabbedPane();
        createBottomBar();

        frame.pack();
        frame.setVisible(true);
    }

    private void createTopBar() {
        this.topBar = new JPanel();
        this.topBar.setLayout(new BorderLayout());
        this.topBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

        JLabel title = new JLabel("LA Foodie");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel badge = new JLabel(new ImageIcon("yelp_powered_btn_red.png"));
        Helpers.setFontSize(title, 24);

        this.topBar.add(userLabel, BorderLayout.WEST);
        this.topBar.add(title, BorderLayout.CENTER);
        this.topBar.add(badge, BorderLayout.EAST);

        frame.getContentPane().add(this.topBar, BorderLayout.NORTH);
    }
    
    private void createTabbedPane() {
        this.tabbedPane = new JTabbedPane();
        this.searchPanel = new JPanel();
        this.favoritesPanel = new JPanel();

        this.searchPanel.setLayout(new GridLayout(10, 1, 0, 5));
        this.favoritesPanel.setLayout(new GridLayout(10, 1, 0, 5));
        this.searchPanel.setBackground(Color.BLACK);

        searchPane = new JScrollPane(this.searchPanel);
        favoritesPane = new JScrollPane(this.favoritesPanel);
        searchPane.getVerticalScrollBar().setUnitIncrement(SCROLL_AMOUNT);
        favoritesPane.getVerticalScrollBar().setUnitIncrement(SCROLL_AMOUNT);

        tabbedPane.addTab("Search", searchPane);
        tabbedPane.addTab("Favorites", favoritesPane);

        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    private void createBottomBar() {
        this.bottomBar = new JPanel();
        this.bottomBar.setLayout(new BorderLayout());
        this.bottomBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JButton button = new JButton("Search again");
        button.addActionListener(new SearchButtonListener(this.app));
        button.setPreferredSize(new Dimension(150, 30));
        this.bottomBar.add(button, BorderLayout.WEST);

        this.cacheLabel = new JLabel("");
        this.statusLabel = new JLabel("");
        Helpers.setFontSize(this.statusLabel, 18);
        this.statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.setCacheLabelText(0, 0);
        this.statusLabel.setText("Loading results...");
        
        this.bottomBar.add(this.cacheLabel, BorderLayout.EAST);
        this.bottomBar.add(this.statusLabel, BorderLayout.CENTER);
        
        frame.getContentPane().add(this.bottomBar, BorderLayout.SOUTH);
    }

    private void setCacheLabelText(int hits, int misses) {
        String stats = "Cache statistics: " + hits + " hits, " + misses + " misses";
        this.cacheLabel.setText(stats);
    }

    private void setStatusLabelText(String location, String cuisine) {
        String status = "Showing results for \"" + cuisine + "\" near " + location;
        this.statusLabel.setText(status);
    }

    public void renderSearchResults(Restaurant[] results) {
        this.searchPanel.removeAll();
        this.searchPanel.validate();
        this.searchPane.validate();

        for (Restaurant r : results) {
            SearchRow row = new SearchRow(this.app, r);
            this.searchPanel.add(row);
        }

        setCacheLabelText(APIRequest.cacheHits, APIRequest.cacheMisses);
        setStatusLabelText(APIRequest.location, APIRequest.term);

        this.searchPanel.validate();
        this.searchPane.validate();
    }

    public void renderFavorites() {
        this.favoritesPanel.removeAll();

        for (Restaurant r : this.app.getFavorites()) {
            FavoriteRow row = new FavoriteRow(this.app, r);
            this.favoritesPanel.add(row);
        }

        this.favoritesPanel.validate();
        this.favoritesPane.validate();
    }

    public void addFavoriteRow(Restaurant r) {
        FavoriteRow row = new FavoriteRow(this.app, r);
        this.favoritesPanel.add(row);
        this.favoritesPanel.validate();
        this.favoritesPane.validate();
    }

    public Application getApplication() {
        return this.app;
    }
    
    public JFrame getFrame() {
        return frame;
    }

    public JScrollPane getSearchPane() {
        return searchPane;
    }

    public JScrollPane getFavoritesPane() {
        return favoritesPane;
    }

    public JPanel getSearchPanel() {
        return searchPanel;
    }

    public JPanel getFavoritesPanel() {
        return favoritesPanel;
    }
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}
