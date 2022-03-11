package cp.week10;

public class excersize4runner {
    
    public static void main(String[] args){
        PersonSet persons = new PersonSet();

        Thread t1 = new Thread(() -> {
            for( int i = 0; i<1000; i++){
                persons.addPerson(new Person(8, "t1:test" + i));
            }
            for( int i = 0; i<1000; i++){
                persons.getMySet().add(new Person(16, "t1:unsafe" + i));
            }
        });

        Thread t2 = new Thread(() -> {
            for( int i = 0; i<1000; i++){
                persons.addPerson(new Person(8, "t2:test" + i));
            }
            for( int i = 0; i<1000; i++){
                persons.getMySet().add(new Person(16, "t2:unsafe" + i));
            }
        });

        Thread t3 = new Thread(() -> {
            for( int i = 0; i<1000; i++){
                persons.addPerson(new Person(8, "t3:test" + i));
            }
            for( int i = 0; i<1000; i++){
                persons.getMySet().add(new Person(16, "t3:unsafe" + i));
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            
            System.out.println(persons.getMySet().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
