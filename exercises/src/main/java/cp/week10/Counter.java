package cp.week10;

public class Counter{

    private int i; //Private to threadsafe bcs it can only be used from methods.

    public Counter(int i){
        this.i = i;
    }

    public int i(){ //Thread-safe synchronized
        synchronized(this){
            return i;
        }
    }

    public void increment(){ //Thread-safe synchronized
        synchronized(this){
            i = i++;
        }
    }

    public void decrement(){ //Thread-safe synchronize
        synchronized(this){
            i = i--;
        }
    }
}