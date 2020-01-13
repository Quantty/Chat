package Server;

import Iterator.Iterator;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Quanticus on 10/4/2017.
 */


public class ClientThread implements Runnable {
    private Socket socket;
    private String username;
    private PrintWriter clientOut;
    private ChatServer server;
    private boolean alive;


    public ClientThread(ChatServer server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
    }

    private PrintWriter getWriter(){
        return clientOut;
    }

    @Override
    public void run() {
        try{
            // setup
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());

            // start communicating
            Boolean connected = false ;
            while(!socket.isClosed()){
                if(in.hasNextLine()){
                    String input = in.nextLine();
                    String str[] = input.split(" ",3);
                    switch(str[0]){
                        case "DATA":
                            messageDATA(str);
                            break;
                        case "IMAV":
                            this.alive = true;
                            break;
                        case "QUIT":
                            clientOut.println("Disconnected!");
                            socket.close();
                            Thread.currentThread().interrupt();


                    }


                    /**
                     * here we try to connect the client to server
                     */
                    if(!connected){
                        if(joinServer(input)){
                            connected = true;
                            clientOut.write("J_OK\r\n");
                            clientOut.flush();
                            this.alive = true;
                            lifeThread(server,this);
                            this.username = input.split(" ")[1];
                            this.username = username.substring(0,username.length()-1);
                            System.out.println(username+" is connected.");
                            updateOnlineClients();
                            continue;
                        }else {
                            clientOut.write("J_ER 400: Could not connect!\r\n");
                            clientOut.flush();
                            socket.close();
                            //Thread.currentThread().interrupt();
                            continue;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateOnlineClients(){
        String txt = "LIST ";
        for(Iterator thatClient = server.getClients().iterator();thatClient.hasNext();){
            txt = txt + thatClient.next().username+" ";
        }
        System.out.println(txt);
        for(Iterator thatClient = server.getClients().iterator(); thatClient.hasNext();) {
            PrintWriter thatClientOut = thatClient.next().getWriter();
            if (thatClientOut != null) {
                thatClientOut.write(txt + "\r\n");
                thatClientOut.flush();
            }

        }
    }

    public void lifeThread(ChatServer server, ClientThread clientThread){
        CheckAlive checkAlive = new CheckAlive(server,clientThread);
        Thread  checkAliveThread = new Thread(checkAlive);
        checkAliveThread.start();
    }

    public boolean getAlive(){
        return alive;
    }

    public void setAlive(boolean alive){
        this.alive = alive;
    }

    public void messageDATA(String[] str){
        if(isClient(server.getClients().iterator(),str[1].substring(0,str[1].length()-1))){
            for(Iterator thatClient = server.getClients().iterator(); thatClient.hasNext();){
                PrintWriter thatClientOut = thatClient.next().getWriter();
                if(thatClientOut != null){
                    System.out.println(str[2]);
                    //if(thatClient.username.equals(str[1].substring(0,str[1].length()-1))) continue;
                    thatClientOut.write("DATA "+str[1]+"  "+str[2] + "\r\n");
                    thatClientOut.flush();
                }
            }
        }
    }

    public boolean isClient(Iterator clients, String name){
        for(Iterator client = clients; clients.hasNext();){
            if (client.next().username.equals(name)){
                return true;
            }
        }
        return false;
    }

    public boolean joinServer(String command) throws IOException {
        if(command.split(" ",3)[0].equals("JOIN")){
//            // i get first word, second and the rest
//            String username = command.split(" ",3)[1];
//            if(username.endsWith(",")) {
//                username = username.substring(0,username.length()-1);
//                for (ClientThread client : server.getClients()) {
//                    // if client is already connected
//                    if (username.equals(client.username)){
//                       // clientOut.write("username exists!");
//                       return false;
//
//                    }
//                }
//            }else{
//               // clientOut.write("username no ,!");
//                return  false;
//            }
//
//        }else {
//           // clientOut.write("Join not at the begining ");
//            return false;
        }
        return true;
    }
}
