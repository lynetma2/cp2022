package cp.week8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.*;
import java.util.*;

public class Exercises{

    public static void main(String[] args){
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        task5();
    }

    public static void task1 (){
        String fileName = "./exercises/src/main/java/cp/week8/test.txt";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.filter(s -> s.endsWith("."))
            .forEach(System.out::println);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void task2(){
        String fileName = "./exercises/src/main/java/cp/week8/test.txt";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            ArrayList<String> linesWithC =  stream.filter(s -> s.startsWith("C"))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            System.out.println(linesWithC);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void task3(){
        String fileName = "./exercises/src/main/java/cp/week8/test.txt";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            long lines = stream.filter(s -> s.contains("L")).count();
            System.out.println(lines);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void task4(){
        String fileName = "./exercises/src/main/java/cp/week8/test.txt";

        //Maps every line to the amount of C's in that line. Sums everything afterwards.
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            int sumOfC = stream.mapToInt(s -> {
                long count = s.chars().filter(ch -> ch == 'C').count();
                return Math.toIntExact(count);
            }).sum();

            System.out.println(sumOfC);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Look at the "countWords" example from the StreamingFiles.java file for more information.
    public static void task5(){
        String fileName = "./exercises/src/main/java/cp/week8/test.txt";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            //Flattens the lines in strings of 1 char length.
            stream.flatMap(s -> Stream.of(s.split("(?!^)")))
            //Changes the strings to hashmaps like the following "char" -> 1 
            .map(ch -> {
                Map<String , Integer> m = new HashMap<>();
                m.put(ch.toString(), 1);
                return m;
            })
            //Combines all the hashmaps
            .reduce( new HashMap<String, Integer>(), (m1, m2) -> {
                Map<String , Integer> result = new HashMap<>( m1 );
                m2.forEach( (key, value) -> result.merge(key, value, Integer::sum));
                return result;
            })
            //prints the result of the hashmap
            .forEach( (ch, value) -> System.out.println(ch + " -> " + value));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}