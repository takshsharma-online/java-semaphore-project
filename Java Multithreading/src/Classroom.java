import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Classroom {

    // Classroom information
    private String name;
    private int capacity;
    private int expectedStudents;

    // Current classroom occupants
    private int students = 0;
    private int visitors = 0;

    // Lecturer information
    private String lecturerName = "None";

    private boolean lecturerInside = false;
    private boolean inSession = false;
    private boolean lectureCompleted = false;

    // Lock protects shared classroom information
    private final ReentrantLock lock =
            new ReentrantLock(true);

    // Condition: controls who can enter
    private final Condition entryAllowed =
            lock.newCondition();

    // Condition: lecturer waits for all students
    private final Condition studentsReady =
            lock.newCondition();

    // Condition: students wait until lecturer leaves
    private final Condition lectureFinished =
            lock.newCondition();

    // Counting semaphore controls classroom capacity
    private final Semaphore capacitySemaphore;

    // Binary semaphore allows only one lecturer
    private final Semaphore lecturerSemaphore =
            new Semaphore(1, true);


    // Constructor
    public Classroom(String name, int capacity,
                     int expectedStudents) {

        this.name = name;
        this.capacity = capacity;
        this.expectedStudents = expectedStudents;

        capacitySemaphore =
                new Semaphore(capacity, true);
    }



    // STUDENT ENTER


    public void studentEnter() throws InterruptedException {

        // Student requests one available space
        capacitySemaphore.acquire();

        lock.lock();

        try {

            // Wait if lecturer is inside
            while (lecturerInside) {
                entryAllowed.await();
            }

            // Student enters classroom
            students++;

            System.out.println(
                    Thread.currentThread().getName()
                            + " entered " + name
            );

            // Notify lecturer when all students arrive
            if (students >= expectedStudents) {
                studentsReady.signalAll();
            }

        } finally {
            lock.unlock();
        }
    }



    // STUDENT LEAVE


    public void studentLeave() throws InterruptedException {

        lock.lock();

        try {

            // Students cannot leave during lecture
            while (lecturerInside) {
                lectureFinished.await();
            }

            students--;

            System.out.println(
                    Thread.currentThread().getName()
                            + " left " + name
            );

            // Release classroom space
            capacitySemaphore.release();

        } finally {
            lock.unlock();
        }
    }



    // VISITOR ENTER


    public void visitorEnter() throws InterruptedException {

        // Request available classroom space
        capacitySemaphore.acquire();

        lock.lock();

        try {

            // Visitors cannot enter during lecture
            while (lecturerInside) {
                entryAllowed.await();
            }

            visitors++;

            System.out.println(
                    Thread.currentThread().getName()
                            + " entered " + name
            );

        } finally {
            lock.unlock();
        }
    }



    // VISITOR LEAVE


    public void visitorLeave() {

        lock.lock();

        try {

            visitors--;

            System.out.println(
                    Thread.currentThread().getName()
                            + " left " + name
            );

            // Release classroom space
            capacitySemaphore.release();

        } finally {
            lock.unlock();
        }
    }



    // LECTURER ENTER


    public boolean lecturerEnter(String lecturer)
            throws InterruptedException {

        // Only one lecturer can acquire this permit
        lecturerSemaphore.acquire();

        // Tracks whether lecturer successfully entered
        boolean entered = false;

        try {

            lock.lock();

            try {

                // Check whether classroom already had a lecture
                if (lectureCompleted) {
                    return false;
                }

                // Wait until every expected student arrives
                while (students < expectedStudents) {
                    studentsReady.await();
                }

                // Lecturer enters classroom
                lecturerInside = true;

                lecturerName = lecturer;

                entered = true;

                System.out.println(
                        lecturer + " entered " + name
                );

                return true;

            } finally {
                lock.unlock();
            }

        } finally {

            // Release semaphore if lecturer did not enter
            if (!entered) {
                lecturerSemaphore.release();
            }

        }
    }


    // START LECTURE

    public void startLecture(String lecturer)
            throws InterruptedException {

        lock.lock();

        try {

            inSession = true;

            System.out.println(
                    lecturer + " started lecture in " + name
            );

        } finally {
            lock.unlock();
        }

        // Simulate a lecture lasting 5 seconds
        Thread.sleep(5000);
    }


    // LECTURER LEAVE
    public void lecturerLeave(String lecturer) {
        lock.lock();
        try {
            System.out.println(
                    lecturer + " left " + name
            );
            // Update classroom status
            inSession = false;
            lecturerInside = false;
            lectureCompleted = true;
            // Allow students to leave
            lectureFinished.signalAll();
            // Allow visitors and students to enter
            entryAllowed.signalAll();
        } finally {
            lock.unlock();
        }
        // Allow another lecturer to acquire the semaphore
        lecturerSemaphore.release();
    }
    // STUDENT WAITS FOR LECTURE COMPLETION
    public void waitForLecture() throws InterruptedException {
        lock.lock();
        try {
            while (!lectureCompleted) {
                lectureFinished.await();
            }
        } finally {
            lock.unlock();
        }
    }
    // Get Classroom status
    public String getStatus() {
        lock.lock();
        try {
            return String.format(
                    "%-12s %-12s %-12s %-12d %-12d",
                    name,
                    lecturerName,
                    inSession,
                    students,
                    visitors
            );
        } finally {
            lock.unlock();
        }
    }
}