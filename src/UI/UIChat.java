package UI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by Quanticus on 12/17/2019.
 */
public class UIChat {
    @FXML
    public VBox vbox ;
    @FXML public TextArea usersOnline,chatBox,console;
    @FXML Button sendBtn;

    public void buildChat(String userName, VBox vbox){
        vbox.getChildren().clear();
        Label usernameLabel = new Label("Username :");
        HBox line1 = new HBox();
        line1.getChildren().addAll(usernameLabel,new Label(userName));
        line1.setSpacing(10);
        HBox line2 = new HBox();

        usersOnline = new TextArea();
        usersOnline.setPrefWidth(100);
        usersOnline.setPrefHeight(300);
        usersOnline.setEditable(false);
        chatBox = new TextArea();
        chatBox.setPrefHeight(300);
        chatBox.setPrefWidth(400);
        chatBox.setEditable(false);

        line2.getChildren().addAll(new VBox(new Label("Users Online"),usersOnline),new VBox(new Label(""),chatBox));
        line2.setSpacing(20);

        HBox line3 = new HBox();
        console = new TextArea();
        console.setPrefWidth(250);
        console.setPrefHeight(50);
        sendBtn = new Button("Send");
        line3.getChildren().addAll(new Label("                        "),console,sendBtn);
        line3.setSpacing(20);
        vbox.getChildren().addAll(line1,line2,line3);

    }


}
