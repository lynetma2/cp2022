package cp.week16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Exercise15
{
	public static void main(String[] args)
	{
		// word -> number of times it appears over all files
		Map< Path, FileInfo > occurrences = new HashMap<>();
		ExecutorService executor = Executors.newWorkStealingPool();
		ExecutorCompletionService< Map< Path, FileInfo > > completionService =
			new ExecutorCompletionService<>( executor );

		try {
			long pendingTasks =
				Files.walk( Paths.get( "lectures/data" ) )
					.filter( Files::isRegularFile )
					.map( filepath -> completionService.submit( () -> {
						Map<Path, FileInfo> map = new HashMap<Path, FileInfo>();
						map.put(filepath, computeOccurrences(filepath));
						return map;
					}))
					.count();
			while( pendingTasks > 0 ) {
				Map< Path, FileInfo > fileOccurrences = completionService.take().get();
				fileOccurrences.forEach( (path, info) -> occurrences.merge( path, info, (v1, v2) -> v1 ) );
				pendingTasks--;
			}
		} catch( InterruptedException | ExecutionException | IOException e ) {
			e.printStackTrace();
		}
		
		try {
			executor.shutdown();
			executor.awaitTermination( 1, TimeUnit.DAYS );
		} catch( InterruptedException e ) {
			e.printStackTrace();
		}
		
		occurrences.forEach( (word, n) -> System.out.println( word + ": " + n ) );
	}
	
	private static FileInfo computeOccurrences( Path textFile )
	{
		FileInfo info = new FileInfo(0, 0, 0);

		try {
			long size = Files.size(textFile);
			long numberOfLines = Files.lines( textFile ).count();
			long numberOfLinesL = Files.lines( textFile ).filter(s -> s.startsWith("L")).count();

			info = new FileInfo(size, numberOfLines, numberOfLinesL);

		} catch( IOException e ) {
			e.printStackTrace();
		}
		
		return info;
	}

	public static class FileInfo {
		long size;
		long numberOfLines;
		long numberOfLinesL;

		public FileInfo(long size, long numberOfLines, long numberOfLinesL){
			this.size = size;
			this.numberOfLines = numberOfLines;
			this.numberOfLinesL = numberOfLinesL;
		}

		public String toString() {
			return "[ size: " + size + ", numberOfLines: " + numberOfLines + ", numberOfLinesL: " + numberOfLinesL + " ]";
		}
	}
}
