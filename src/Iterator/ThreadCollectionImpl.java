package Iterator;

import Server.ClientThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quanticus on 12/18/2019.
 */
public class ThreadCollectionImpl implements ThreadCollection {
    private List<ClientThread> threadList;

    public ThreadCollectionImpl(){
        threadList =  new ArrayList<>();
    }

    public void addThread(ClientThread c){
        this.threadList.add(c);
    }

    public void removeThread(ClientThread c){
        this.threadList.remove(c);
    }
    @Override
    public Iterator iterator(){
        return new IteratorImpl(this.threadList);
    }

    private class IteratorImpl implements Iterator{
        private List<ClientThread> threads;
        private int position;

        public IteratorImpl(List<ClientThread> list){
            this.threads = list;
        }
        @Override
        public boolean hasNext(){
            if (position < threads.size()){
                return true;
            }else  return false;
        }
        @Override
        public ClientThread next(){
            ClientThread obj = threads.get(position);
            this.position ++;
            return obj;
        }
    }
}
