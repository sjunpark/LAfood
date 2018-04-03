package gui;

import java.awt.event.ActionEvent;
import api.Restaurant;
import application.Application;
import javax.swing.JOptionPane;

public class AddFavoriteListener extends AbstractButtonListener {
    private Restaurant restaurant;
    public static final int MAX_FAVORITES = 5;
    
    public AddFavoriteListener(Application app, Restaurant r) {
        super(app);
        this.restaurant = r;
    }

    // add to favorites
    public void actionPerformed(ActionEvent event) {
        if (this.app.getFavorites().size() > MAX_FAVORITES) {
            JOptionPane.showMessageDialog(  this.app.getGui().getFrame(),
                                            "You can not have more than " + MAX_FAVORITES + " favorites!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            this.app.addToFavorites(this.restaurant);
            this.app.getDb().addFavorite(this.app.getUser(), this.restaurant.getId());
        }
    }
}