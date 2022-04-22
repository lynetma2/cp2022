package cp.week16;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cp.week11.Words;

public class Exercise13
{
	public static void main()
	{
        Map< Character, Set<String>> charSet = new ConcurrentHashMap<>();
		//ExecutorService executor = Executors.newCachedThreadPool();
		ExecutorService executor = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );
		//ExecutorService executor = Executors.newFixedThreadPool( 4 );
		
        try {
			Files.walk( Paths.get("lectures/data") )
				.filter( Files::isRegularFile )
				.forEach( filepath -> {
					executor.submit( () -> {
						computeSet(filepath, charSet);
					} );
				} );
        } catch ( IOException e) {
            e.printStackTrace();
        }

		try {
			executor.shutdown();
			executor.awaitTermination( 1, TimeUnit.DAYS );
		} catch( InterruptedException e ) {
			e.printStackTrace();
		}
		
        //charSet.forEach( (word, n) -> System.out.println( word + ": " + n ) );
	}
	
	private static void computeSet( Path filename, Map< Character, Set<String>> words) {
		try {
			Files.lines( filename )
				.flatMap( Words::extractWords )
				.forEach(s -> {
					Character c = s.charAt(0);
                    Set<String> newSet = new HashSet<>();
                    newSet.add(s);
					words.merge(c, newSet, (v1, v2) -> {
                        v1.add(s);
                        return v1;
                    } );
				});
		} catch (IOException e ) {
            e.printStackTrace();
        }
	}
}