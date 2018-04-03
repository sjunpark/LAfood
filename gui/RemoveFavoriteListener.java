package gui;

import java.awt.event.ActionEvent;
import api.Restaurant;
import application.Application;

public class RemoveFavoriteListener extends AbstractButtonListener {
    private Restaurant restaurant;
    private FavoriteRow row;

    public RemoveFavoriteListener(Application app, Restaurant r, FavoriteRow row) {
        super(app);
        this.restaurant = r;
        this.row = row;
    }

    public void actionPerformed(ActionEvent event) {
        this.app.getDb().removeFavorite(this.app.getUser(), this.restaurant.getId());
        // Remove the row from the list
        app.getFavorites().remove(this.restaurant);
        app.getGui().getFavoritesPanel().remove(row);
        app.getGui().getFavoritesPanel().validate();
    }
}
