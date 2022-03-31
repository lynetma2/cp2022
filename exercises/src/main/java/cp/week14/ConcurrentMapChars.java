package cp.week14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import cp.week11.Words;

public class ConcurrentMapChars
{
	public static void main(String args[])
	{
        Map< Character, Set<String>> charSet = new ConcurrentHashMap<>();
        List< Thread > threads = new ArrayList<>();
		
        try {
			Files.walk( Paths.get("lectures/data") )
				.filter( Files::isRegularFile )
				.map( filename -> new Thread( () -> {
					computeSet(filename, charSet);
				} ) )
				.forEach( thread -> threads.add(thread) );
        } catch ( IOException e) {
            e.printStackTrace();
        }

		threads.forEach( Thread::start );
		threads.forEach( t -> {
			try {
				t.join();
			} catch( InterruptedException e ) {
				e.printStackTrace();
			}
		} );
		
        charSet.forEach( (word, n) -> System.out.println( word + ": " + n ) );
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