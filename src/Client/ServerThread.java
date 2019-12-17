package Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Quanticus on 10/4/2017.
 */
public class ServerThread implements Runnable {
    private Socket socket;
    private String userName;
    @FXML private TextArea usersOnline,chatBox;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;
    private String onlineClients = "";

    public ServerThread(Socket socket, String userName,TextArea usersOnline,TextArea chatBox ){
        this.socket = socket;
        this.userName = userName;
        messagesToSend = new LinkedList<>();
        this.usersOnline = usersOnline;
        this.chatBox = chatBox;
    }

    public void addNextMessage(String message){
        synchronized (messagesToSend){
            hasMessages = true;
            messagesToSend.push(message);
        }
    }

    @Override
    public void run(){

        //System.out.println("Local Port :" + socket.getLocalPort());
        //System.out.println("Server = " + socket.getRemoteSocketAddress() + ":" + socket.getPort());

        try{
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInStream);

            if(!socket.isClosed()){
                serverOut.println("JOIN "+userName+", "+socket.getInetAddress()+":"+5000+"\r\n");
                serverOut.flush();
            }
            String inn = serverIn.nextLine();
            if (inn.equals("J_OK")) {
                System.out.println("You are connected");
                aliveThread(socket);

            }else if (inn.equals("J_ER")){
                System.out.println(inn);
            }

            while(!socket.isClosed()){
                if(serverInStream.available() > 0){
                    if(serverIn.hasNextLine()){
                        String text = serverIn.nextLine();
                        switch(text.split(" ",3)[0]){
                            case "DATA":
                               // System.out.println(text.split(" ",2)[1]);
                                chatBox.setText(chatBox.getText()+"\n"+text.split(" ",2)[1]);
                                break;
                            case "LIST":
                                System.out.print(text.substring(5).replace(" ", "\n"));
                                usersOnline.setText(text.substring(5).replace(" ", "\n"));
                                break;
                        }
                    }
                }
                if(hasMessages){
                    String nextSend = "DATA "+userName+": ";
                    synchronized(messagesToSend){
                        nextSend = nextSend + messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    // message to the server
                    serverOut.println(nextSend);
                    serverOut.flush();
                }
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }
    public void aliveThread(Socket socket) throws IOException {
        LifePulse lifePulse = new LifePulse(socket);
        Thread lifePulseThread = new Thread(lifePulse);
        lifePulseThread.start();
        // stop the alive messages when the client disconnects.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                lifePulseThread.interrupt();


            }
        });
    }
}
