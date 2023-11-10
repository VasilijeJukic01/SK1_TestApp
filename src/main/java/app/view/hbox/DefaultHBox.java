package app.view.hbox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class DefaultHBox extends HBox {

    public DefaultHBox(Pos position, Node... children) {
        super(children);
        super.setAlignment(position);
        super.setPadding(new Insets(5));
        super.setSpacing(5);
    }
}
