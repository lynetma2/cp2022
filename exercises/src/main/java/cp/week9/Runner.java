package cp.week9;

public class Runner {
    public static void main(String[] args) {
        Utils.doAndMeasure( () -> {
            SynchronizedMap2TModified.main();
        } );  
        Utils.doAndMeasure( () -> {
            SynchronizedMap.main();
        } );  
    }
}
