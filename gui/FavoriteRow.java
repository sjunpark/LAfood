package gui;

import java.awt.Color;

import api.Restaurant;
import application.Application;

@SuppressWarnings("serial")
public class FavoriteRow extends SearchRow {
    public FavoriteRow(Application app, Restaurant r) {
        super(app, r);

        this.button.setText("Remove from favorites");
        this.button.removeActionListener(this.button.getActionListeners()[0]);
        this.button.addActionListener(new RemoveFavoriteListener(this.application, this.restaurant, this));
    }

    @Override
    protected Color getBackgroundColor() {
        return new Color(148, 255, 173);
    }
}
