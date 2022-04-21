package cp.week16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Exercise18 {
	public static void main(String[] args)
	{
		// word -> number of times it appears over all files
		boolean found = false;
		Map< Path, FileInfo > occurrences = new ConcurrentHashMap<>();

		try {
			CompletableFuture< Map< Path, FileInfo> >[] futures =
				Files.walk( Paths.get( "lectures/data" ) )
					.filter( Files::isRegularFile )
					.map( filepath ->
						CompletableFuture.supplyAsync( () -> {
							Map<Path, FileInfo> map = new HashMap<Path, FileInfo>();
							map.put(filepath, computeOccurrences(filepath));
							return map; }) 
							.thenAccept( fileOccurrences ->
								fileOccurrences.forEach( (path, info) -> occurrences.merge( path, info, (v1, v2) -> v1 ) )
							)
					).collect( Collectors.toList() ).toArray( new CompletableFuture[0] );
			
				while (!CompletableFuture.allOf(futures).isDone() && !found){
					Map<Path, FileInfo> result = (Map<Path, FileInfo>) CompletableFuture.anyOf(futures).get();
					for (FileInfo fi : result.values()) {
						if (fi.numberOfLines > 10)
							found = true;
					}
				}
		} catch( IOException | InterruptedException | ExecutionException e ) {
			e.printStackTrace();
		}
		
		occurrences.forEach( (word, n) -> System.out.println( word + ": " + n ) );
	}

	private static FileInfo computeOccurrences(Path textFile) {
		FileInfo info = new FileInfo(0, 0, 0);

		try {
			long size = Files.size(textFile);
			long numberOfLines = Files.lines(textFile).count();
			long numberOfLinesL = Files.lines(textFile).filter(s -> s.startsWith("L")).count();

			info = new FileInfo(size, numberOfLines, numberOfLinesL);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return info;
	}

	public static class FileInfo {
		long size;
		long numberOfLines;
		long numberOfLinesL;

		public FileInfo(long size, long numberOfLines, long numberOfLinesL) {
			this.size = size;
			this.numberOfLines = numberOfLines;
			this.numberOfLinesL = numberOfLinesL;
		}

		public String toString() {
			return "[ size: " + size + ", numberOfLines: " + numberOfLines + ", numberOfLinesL: " + numberOfLinesL
					+ " ]";
		}
	}
}
