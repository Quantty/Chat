package Iterator;

import Server.ClientThread;

/**
 * Created by Quanticus on 12/18/2019.
 */
public interface Iterator {
    public boolean hasNext();
    public ClientThread next();
}
