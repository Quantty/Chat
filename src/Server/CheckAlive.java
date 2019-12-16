package Server;

/**
 * Created by Quanticus on 10/12/2017.
 */
public class CheckAlive implements Runnable {
    private boolean alive = true;
    private ChatServer server;
    private ClientThread clientThread;

    public CheckAlive(ChatServer server,ClientThread clientThread){
        this.server = server;
        this.clientThread = clientThread;
    }
    @Override
    public void run(){
        while (alive) {
            alive = false;
            clientThread.setAlive(false);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            alive = clientThread.getAlive();
        }
        server.getClients().remove(clientThread);
    }
}
