import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Character.getName;

public class Visitor extends Thread{
    private Classroom classroom;
    //Constructor
    public Visitor(String name, Classroom classroom){
        super(name);
        this.classroom = classroom;
    }
    @Override
    public void run() {
        try {
            // Visitor enters classroom
            classroom.visitorEnter();
            System.out.println(
                    getName() + " is visiting the classroom."
            );
            // Visitor stays between 2 and 6 seconds
            int time = ThreadLocalRandom.current().nextInt(2000, 6000);
            Thread.sleep(time);
            // Visitor leaves classroom
            classroom.visitorLeave();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(
                    getName() + " was interrupted."
            );
        }
    }
}

