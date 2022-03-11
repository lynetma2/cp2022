package cp.week10;
import cp.week9.Utils;

public class Runner {
    public static void main(String[] args) {
        Utils.doAndMeasure( () -> {
            countWords.main(true);
        } );
    }
}
