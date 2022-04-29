package cp.week17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Exercise21
{
	public static void main()
	{
		try {
			boolean found =
				Files
					.walk( Paths.get( "lectures/data" ) )
					.filter( Files::isRegularFile )
					.collect( Collectors.toList() )
					.parallelStream()
					.mapToLong( textFile -> {
						try {
							return Files.lines( textFile ).count();
						} catch( IOException e ) {
							return 0l;
						}
					} )
					.anyMatch( numberOfLines -> numberOfLines > 10 );
			System.out.println( found );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
}
