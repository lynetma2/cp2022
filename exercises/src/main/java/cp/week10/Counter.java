package cp.week10;

public class Counter{

    private int i; //Private to threadsafe bcs it can only be used from methods.

    public Counter(int i){
        this.i = i;
    }

    public synchronized int i(){ //Thread-safe synchronized
        return i;
    }

    public synchronized void increment(){ //Thread-safe synchronized
        i = i++;
    }

    public synchronized void decrement(){ //Thread-safe synchronized
        i = i--;
    }
}