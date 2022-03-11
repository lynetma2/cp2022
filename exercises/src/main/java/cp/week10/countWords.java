package cp.week10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class countWords {
    
    public static Map<String, Integer> computeOccurencesSequential (Stream< String > filenames ){
        Map< String, Integer > occurrences = new HashMap<>();

        filenames.forEach(s -> computeOccurrences(s, occurrences) );

        return occurrences;
    }

    public static Map<String, Integer> computeOccurencesConcurrent (Stream< String > filenames ){
        Map< String, Integer > occurrences = new HashMap<>();
        List< Thread > threads = new ArrayList<>();

        filenames.forEach(s -> {
            Thread t = new Thread(() -> computeOccurrences(s, occurrences));
            threads.add(t);
        } );

        for (Thread thread : threads) {
            thread.start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return occurrences;
    }

    private static void computeOccurrences(String filename, Map<String, Integer> occurrences) {
		try {
			Files.lines( Paths.get( filename ) ).flatMap( Words::extractWords ).map( String::toLowerCase ).forEach( s -> {
				synchronized( occurrences ) {
					occurrences.merge( s, 1, Integer::sum );
				}
			} );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
}
