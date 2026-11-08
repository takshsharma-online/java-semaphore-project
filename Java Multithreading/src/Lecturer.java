import java.util.concurrent.ThreadLocalRandom;
public class Lecturer extends Thread{
    private Classroom[] classrooms;
    //Constructor
    public Lecturer(String name, Classroom[] classrooms){
        super(name);
        this.classrooms = classrooms;
    }
    @Override
    public void run() {
        // Choose a random starting classroom
        int start = ThreadLocalRandom.current()
                .nextInt(classrooms.length);
        // Visit each classroom
        for (int i = 0; i < classrooms.length; i++) {
            Classroom classroom =
                    classrooms[(start + i) % classrooms.length];
            boolean entered = false;
            try {
                // Lecturer attempts to enter classroom
                entered = classroom.lecturerEnter(getName());
                // Skip if classroom already completed its lecture
                if (!entered) {
                    continue;
                }
                // Start lecture
                classroom.startLecture(getName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                // Ensure lecturer leaves if they entered
                if (entered) {
                    classroom.lecturerLeave(getName());
                }
            }
        }
    }}
