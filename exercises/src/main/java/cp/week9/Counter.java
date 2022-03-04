package cp.week9;

public class Counter {
    
    private int i;

    public Counter(int i){
        this.i = i;
    }

    public int i(){
        return i;
    }

    public synchronized void increment(){
        i++;
    }

    public synchronized void decrement(){
        i--;
    }
}
