package cp.week17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import cp.week11.Words;

public class Exercise20
{
	public static void main()
	{
		try {
			Map<Path, FileInfo> occurrences =
				Files
					.walk( Paths.get( "lectures/data" ) )
					.filter( Files::isRegularFile )
					.collect( Collectors.toList() )
					.parallelStream()
					.filter(textFile -> {
						try {
							return Files.lines(textFile).count() > 10;
						} catch (IOException e) {
							e.printStackTrace();
						}
						return false;
					})
					.collect( Collectors.toMap(
						textFile -> textFile,
						textFile -> computeFile(textFile),
						(v1, v2) -> v1
					) );
			occurrences.forEach( (word, n) -> System.out.println( word + ": " + n ) );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}	

	private static FileInfo computeFile(Path textFile) {
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

		@Override
		public String toString() {
			return "[ size: " + size + ", numberOfLines: " + numberOfLines + ", numberOfLinesL: " + numberOfLinesL
					+ " ]";
		}
	}
}