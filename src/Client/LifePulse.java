package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Quanticus on 10/12/2017.
 */
public class LifePulse implements Runnable {
    private Socket socket;
    private PrintWriter serverOut;

    public LifePulse(Socket socket) throws IOException {
        this.socket = socket;
        this.serverOut = new PrintWriter(socket.getOutputStream());
    }
    @Override
    public void run(){
        while (!socket.isClosed()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            serverOut.println("IMAV\r\n");
            serverOut.flush();
        }
    }
}
