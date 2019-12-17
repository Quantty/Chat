package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
/**
 * Created by Quanticus on 10/4/2017.
 */
public class ChatServer {

    private static final int portNumber = 5000;
    private static ChatServer ServerListener = null;
    private int serverPort;
    private ArrayList<ClientThread> clients;

    public static void main(String[] args){

        ChatServer server = ChatServer.getInstance();
        server.startServer();
    }

    private ChatServer(int portNumber){
        this.serverPort = portNumber;
    }

    public static ChatServer getInstance(){
        synchronized (ChatServer.class){
            if(ServerListener == null){
                ServerListener = new ChatServer(portNumber);
            }
        }
        return  ServerListener;
    }

    public ArrayList<ClientThread> getClients(){
        return clients;
    }

    private void startServer(){
        clients = new ArrayList<>();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }

    private void acceptClients(ServerSocket serverSocket){

        System.out.println("Server started on port  :  " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("Client connecting at : "+ socket.getRemoteSocketAddress());
                System.out.println(serverSocket.getInetAddress());
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
    }

}