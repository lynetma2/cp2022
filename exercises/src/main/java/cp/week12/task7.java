package cp.week12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class task7
{
	public static void main()
	{
		ArrayList<Integer> totalL = new ArrayList<>();

		// word -> number of times that it appears over all files
		Map< String, Integer > occurrences = new HashMap<>();
		
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
				int tot = computeOccurrences( filename, occurrences );
				synchronized(totalL) {
					totalL.add(tot);
				}
				latch.countDown();
			} ) )
			.forEach( Thread::start );
		try {
			latch.await();
		} catch( InterruptedException e ) {
			e.printStackTrace();
		}
		
		occurrences.forEach( (word, n) -> System.out.println( word + ": " + n ) );
		System.out.println(totalL);
		Integer sum = 0;
		for (Integer local: totalL) {
			sum = sum + local;
		}
		System.out.println("Total sum: " + sum);
	}
	
	private static int computeOccurrences( String filename, Map< String, Integer > occurrences )
	{
		try {
			Files.lines( Paths.get( filename ) )
				.flatMap( Words::extractWords )
				.map( String::toLowerCase )
				.forEach( s -> {
					synchronized( occurrences ) {
						occurrences.merge( s, 1, Integer::sum );
					}
				} );

			Long l = Files.lines( Paths.get( filename ) )
			.flatMap( Words::extractWords )
			.filter(s -> s.startsWith("L") )
			.count();

			int numberL = l.intValue();

			return numberL;
		} catch( IOException e ) {
			e.printStackTrace();
			return 0;
		}
	}
}
