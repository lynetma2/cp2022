package cp.week14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import cp.week11.Words;

public class ConcurrentMapExercise9
{
	public static void main(String[] args)
	{
		// word -> number of times that it appears over all files
		Map< Character, Set<String>> charSet = new ConcurrentHashMap<>();
		
		List< String > filenames = List.of(
			"lectures/text1.txt",
			"lectures/text2.txt",
			"lectures/text3.txt",
			"lectures/text4.txt",
			"lectures/text5.txt",
			"lectures/text6.txt",
			"lectures/text7.txt",
			"lectures/text8.txt",
			"lectures/text9.txt",
			"lectures/text10.txt"
		);
		
		CountDownLatch latch = new CountDownLatch( filenames.size() );
		
		filenames.stream()
			.map( filename -> new Thread( () -> {
				computeSet(filename, charSet);
				latch.countDown();
			} ) )
			.forEach( Thread::start );

		try {
			latch.await();
		} catch( InterruptedException e ) {
			e.printStackTrace();
		}
		
		charSet.forEach( (word, n) -> System.out.println( word + ": " + n ) );
	}
	
	private static void computeSet( String filename, Map< Character, Set<String>> words) {
		try {
			Files.lines( Paths.get( filename ) )
				.flatMap( Words::extractWords )
				.forEach(s -> {
					Character c = s.charAt(0);
                    Set<String> newSet = new HashSet<>();
                    newSet.add(s);
					words.merge(c, newSet, (oldvalue, newvalue) -> {
                        oldvalue.add(s);
                        return oldvalue;
                    } );
				});
		} catch (IOException e ) {
            e.printStackTrace();
        }
	}
}
