import sun.misc.Lock;

public class counter{

    private Lock lock = new Lock();
    private int count = 0;

    public int inc() {
        try {
            lock.lock();
            int newCount = ++count;
            lock.unlock();
            return newCount;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}