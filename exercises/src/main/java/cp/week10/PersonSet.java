package cp.week10;

import java.util.HashSet;
import java.util.Set;

//Listing 4.2
//Excersize 4
public class PersonSet {
    private final Set<Person> mySet = new HashSet<Person>();

    public synchronized void addPerson(Person p) {
        mySet.add(p);
    }

    public synchronized boolean containsPerson(Person p){
        return mySet.contains(p);
    }

    public Set<Person> getMySet(){
        return mySet;
    }
}
