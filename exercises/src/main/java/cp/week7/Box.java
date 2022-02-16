package cp.week7;
import java.util.*;
//exercise 1

//Generic class of box with nested type T
public class Box< T > {
    
    //class attribute with content
    private final T content;

    //Constructor with content as argument to create the box
    public Box (T content) {
        //No content no box
        if (content == null) {
            throw new IllegalArgumentException();
        } else {
            this.content = content;
        }
    }

    //Getter for the field content
    public T content(){
        return content;
    }

    //exercise 2
    //Apply the given BoxFunction to the content of the box
    public <O> O apply(BoxFunction<T, O> boxFunction){
        return boxFunction.apply(content);
    }


    //exercise 5
    //Method which applies BoxFunction to all boxes in a list
    //R is the nested return type which is given in the Boxfunction therefore they are the same.
    //E is the type of content inside the Box
    static public <R, E> ArrayList<R> applyToAll(ArrayList<Box<E>> list, BoxFunction<E, R> boxFunction){
        ArrayList<R> result = new ArrayList<>();

        for(Box<E> box: list){
            result.add(box.apply(boxFunction));
        }
        
        return result;
    }
}
