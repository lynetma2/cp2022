package cp.week9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SynchronizedMap2TModified {
	public static void main() {
		Map< Character, Long > occurrences1 = new HashMap<>();
		Map< Character, Long > occurrences2 = new HashMap<>();
		Map< Character, Long > occurrencesMerged = new HashMap<>();

		Thread t1 = new Thread( () ->
			countLetters( Paths.get( "./lectures/data/text1.txt" ), occurrences1 ) );
		Thread t2 = new Thread( () ->
			countLetters( Paths.get( "./lectures/data/text2.txt" ), occurrences2 ) );

		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
					

			occurrences1.forEach((key, value) -> occurrencesMerged.merge(key, value, Long::sum));
			occurrences2.forEach((key, value) -> occurrencesMerged.merge(key, value, Long::sum));
		} catch( InterruptedException e ) {
			e.printStackTrace();
		}
		
		System.out.println( "e -> " + occurrencesMerged.get( 'e' ) );
	}

	private static void countLetters( Path textPath, Map< Character, Long > occurrences ) {
		try( Stream< String > lines = Files.lines( textPath ) ) {
			lines.forEach( line -> {
				for( int i = 0; i < line.length(); i++ ) {
					final char c = line.charAt( i );
					synchronized (occurrences) {
						occurrences.merge( c, 1L, Long::sum );
					}
				}
			} );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
}

/*
Answers to the questions
- Does the resulting code work? Can you explain why?
	- It works bcs there is no shared data being handled on different threads.
- Does the resulting code perform better or worse than the original example SynchronizedMap2T?
	- It performs better 
- Can you hypothesise why?
	- bcs the program has way less time waiting for acces to write and read.
*/