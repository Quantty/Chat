package Iterator;

import Server.ClientThread;

/**
 * Created by Quanticus on 12/18/2019.
 */
public interface ThreadCollection {
    public void addThread(ClientThread t);
    public  void removeThread(ClientThread t);
    public Iterator iterator();
}
