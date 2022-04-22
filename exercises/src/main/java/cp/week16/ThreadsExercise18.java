package cp.week16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise18 {
	/*
	Adapt your program from ThreadsExercise16 to stop as soon as a task that has processed a file with more than 10 lines is completed.

	Hint: use CompletableFuture.anyOf
	*/


	private static class FileInfo {
		private final long size;
		private long lines = 0;
		private long linesStartingWithL = 0;

		FileInfo( long size ) {
			this.size = size;
		}


		long lines() {
			return lines;
		}

		void countLine( String line ) {
			if( line.startsWith( "L" ) ) {
				linesStartingWithL++;
			}
			lines++;
		}

		@Override
		public String toString() {
			return "FileInfo [lines=" + lines + ", L-lines=" + linesStartingWithL + ", size=" + size + "]";
		}


	}

	public static void main(String[] args) {
		// path -> file info
		Map< Path, FileInfo > filesInfo = new ConcurrentHashMap<>();

		try {
			Set< CompletableFuture< Map< Path, FileInfo > > > futures =
				Files.walk( Paths.get( "lectures/data" ) )
					.filter( Files::isRegularFile )
					.map( filepath -> CompletableFuture.supplyAsync( () -> computeFileInfo( filepath ) )
						.thenApply( fileInfo -> {
							filesInfo.putAll( fileInfo );
							return fileInfo;
						} ) )
					.collect( Collectors.toSet() );
			Boolean done = false;
			while( !futures.isEmpty() && !done ) {

				CompletableFuture< Map< Path, FileInfo > >[] futuresArray =
					futures.toArray( new CompletableFuture[ 0 ] );

				Map< Path, FileInfo > info =
					(Map< Path, FileInfo >) CompletableFuture.anyOf( futuresArray ).join();

				futures.removeIf( future -> future.isDone() && future.join().equals( info ) );
				done = info.values().stream().anyMatch( i -> i.lines() > 10 );
			}

			if( done ) {
				futures.forEach( future -> future.cancel( true ) );
			}
		} catch( IOException e ) {
			e.printStackTrace();
		}

				filesInfo.forEach( (path, info) -> System.out.println( path + ": " + info ) );
	}

	private static Map< Path, FileInfo > computeFileInfo( Path regularFile ) {
		Map< Path, FileInfo > fileInfo = new HashMap<>();
		try {
			FileInfo info = new FileInfo( Files.size( regularFile ) );
			Files.lines( regularFile ).forEach( info::countLine );
			fileInfo.put( regularFile, info );
		} catch( IOException e ) {
			e.printStackTrace();
		}
		return fileInfo;
	}
}
