package Client;
import UI.UIChat;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.Socket;

public class Client {
    @FXML public VBox vbox ;
    @FXML public TextField usernameField,ipField,portField;
    private   int portNumber = 5000;
    @FXML public TextArea usersOnline,chatBox,console;
    @FXML Button sendBtn;
    private String userName;
    private String serverHost;

    @FXML
    public void initialize(){
        Label username = new Label("Username");
        Label ip = new Label("IP");
        Label port = new Label("Port");
        username.setMinWidth(100);
        ip.setMinWidth(100);
        port.setMinWidth(100);
        vbox.setMinWidth(400);
        vbox.setMinHeight(300);
        HBox line1 = new HBox();
        HBox line2 = new HBox();
        HBox line3 = new HBox();
        usernameField = new TextField();
        ipField = new TextField();
        ipField.setText("localhost");
        portField = new TextField();
        portField.setText("5000");
        usernameField.setMinWidth(100);
        ipField.setMinWidth(100);
        portField.setMinWidth(100);
        line1.getChildren().addAll(username,usernameField);
        line2.getChildren().addAll(ip,ipField);
        line3.getChildren().addAll(port,portField);

        Button startBtn = new Button();
        startBtn.setMinWidth(100);
        startBtn.setText("Start");

        startBtn.setOnAction((event) -> {
            getData();
        });

        vbox.getChildren().addAll(line1,line2,line3,startBtn);
    }


    public void getData(){
        this.userName = usernameField.getText();
        this.serverHost = ipField.getText();
        this.portNumber = Integer.valueOf(portField.getText());

        buildChat(userName, vbox);

        startClient();
    }



    private void startClient(){
        try{
            Socket socket = new Socket(serverHost, portNumber);
            Thread.sleep(1000); // network needs time

            ServerThread serverThread = new ServerThread(socket, userName,usersOnline,chatBox);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();


            sendBtn.setOnAction((event) -> {
                serverThread.addNextMessage(console.getText());
                console.clear();
            });
        }catch(IOException ex){
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        }catch(InterruptedException ex){
            System.out.println("Interrupted");
        }
    }

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