package gui;

import api.Restaurant;
import api.Location;
import application.Application;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class SearchRow extends JPanel {
    private JLabel snippet;
    private JLabel addressLabel;
    private JLabel imageLabel;
    private JPanel namePanel;
    private ImageIcon image;
    protected JButton button;
    protected Application application;
    protected Restaurant restaurant;
    public static final int HEIGHT = 100;

    public SearchRow(Application app, Restaurant r) {
        this.restaurant = r;
        this.application = app;
        
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setPreferredSize(new Dimension(GUI.WIDTH, HEIGHT));

        this.image = Helpers.getRemoteImage(this.restaurant.getImageUrl());
        this.imageLabel = new JLabel(this.image);
        
        String address = String.join("<br>", r.getLocation().getDisplay_address());
        
        this.addressLabel = new JLabel("<html>" + address + "</html>");
        this.addressLabel.setPreferredSize(new Dimension(180, 100));
        
        //this.snippet = new JLabel("<html>\"" + this.restaurant.getSnippet_text() + "\"</html>");
        this.snippet = new JLabel(this.restaurant.getSnippet_text());
        this.snippet.setPreferredSize(new Dimension(300, HEIGHT));

        buildNamePanel();
        buildWholePanel();
    }

    private void buildNamePanel() {
        this.namePanel = new JPanel();
        this.namePanel.setOpaque(false);
        this.namePanel.setLayout(new BoxLayout(this.namePanel, BoxLayout.Y_AXIS));
        this.namePanel.setPreferredSize(new Dimension(300, HEIGHT));
        
        JLabel titleLabel = new JLabel(this.restaurant.getName());
        JLabel reviewCount = new JLabel(this.restaurant.getReview_count() + " reviews");
        JLabel ratingImg = new JLabel(Helpers.getRemoteImage(this.restaurant.getRating_img_url()));

        Helpers.setFontSize(titleLabel, 20);

        button = new JButton("Add to favorites");
        button.addActionListener(new AddFavoriteListener(this.application, this.restaurant));

        this.namePanel.add(titleLabel);
        this.namePanel.add(ratingImg);
        this.namePanel.add(reviewCount);
        this.namePanel.add(button);
    }
    
    private void buildWholePanel() {
        this.setBackground(getBackgroundColor());
        this.setPreferredSize(new Dimension(1000, 120));
        this.add(imageLabel);
        this.add(namePanel);
        this.add(addressLabel);
        this.add(snippet);
    }

    protected Color getBackgroundColor() {
        return new Color(171, 234, 255);
    }
}
