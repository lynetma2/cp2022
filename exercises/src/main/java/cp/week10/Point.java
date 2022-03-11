package cp.week10;

public class Point {
    public Counter counterOne;
    public Counter counterTwo;

    public Point(){
        counterOne = new Counter(0);
        counterTwo = new Counter(0);  
    }
    
    public boolean areEqual(){
        return counterOne.i() == counterTwo.i();
    }
}
