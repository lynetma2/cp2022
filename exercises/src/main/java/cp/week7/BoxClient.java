package cp.week7;

import java.util.*;

public class BoxClient {
    
    public static void main(String[] args) {
        ArrayList<String> myList = new ArrayList<>();
        myList.add("Cheesecake");
        myList.add("Icecream");
        myList.add("Fruit");
        myList.add("Bread");
        myList.add("Cake");

        Box<ArrayList<String>> myBox = new Box<>(myList);

        //Exercise 3
        myBox.apply(content -> {
            content.sort((String e1, String e2) -> e1.compareTo(e2));
            return content;
        });

        //To see the changed output
        for (String element: myBox.content()){
            System.out.println(element);
        }

        //Exercise 4
        //Calculating the sum of the length of the words
        int littleSum = myBox.apply(content -> {
            Integer sum = 0;
            for (String element: content){
                sum = sum + element.length();
            }
            return sum;
        });
        System.out.println(littleSum);

        //Created for testing exercise 5 solution
        ArrayList<Box<ArrayList<String>>> boxList = new ArrayList<>();
        boxList.add(myBox);
        boxList.add(myBox);
        boxList.add(myBox);
        boxList.add(myBox);
        
        ArrayList<Integer> sums = Box.applyToAll(boxList, box -> {
            Integer sum = 0;
            for (String element: box){
                sum = sum + element.length();
            }
            return sum;
        });

        System.out.println(sums);
    }
}
