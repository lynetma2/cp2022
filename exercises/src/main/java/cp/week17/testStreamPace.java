package cp.week17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class testStreamPace {
    public static void main() {
        try {
            boolean found = Files
                    .walk(Paths.get("lectures/data"))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList())
                    .parallelStream()
                    .map(textFile -> {
                        try {
                            return Files.lines(textFile).count();
                        } catch (IOException e) {
                            return Stream.empty();
                        }
                    })
                    .filter(numberOfLines -> {
                        for (long l = 0L; l < (long) numberOfLines; l++) {
                            //Nothing just to be slow.
                        }
                        return true;
                    })
                    .anyMatch(numberOfLines -> (long) numberOfLines > 10);
            System.out.println(found);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
