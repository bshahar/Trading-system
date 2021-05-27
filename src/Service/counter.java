package Service;

import Persistence.CounterWrapper;
import sun.misc.Lock;

public class counter{

    private Lock lock = new Lock();
    private int count;
    private String name;
    private CounterWrapper counterWrapper;

    public counter(int count,String name){
        this.count=count;
        this.name=name;
        counterWrapper= new CounterWrapper();
    }

    public counter(String name){
        this.count=0;
        this.name=name;
        counterWrapper= new CounterWrapper();
    }

    public int inc() {
        try {

            lock.lock();
            int newCount = ++count;
            counterWrapper.incAndGet(name);
            lock.unlock();
            return newCount;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}