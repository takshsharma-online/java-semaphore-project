public class Monitor extends Thread {

    private Classroom[] classrooms;


    // Constructor
    public Monitor(Classroom[] classrooms) {

        super("Monitor");

        this.classrooms = classrooms;
    }
    @Override
    public void run() {
        try {
            // Monitor continues until interrupted
            while (!Thread.currentThread().isInterrupted()) {

                System.out.println("\n");
                System.out.println(
                        "=============================================================="
                );
                System.out.printf(
                        "%-12s %-12s %-12s %-12s %-12s%n",
                        "Classroom",
                        "Lecturer",
                        "InSession",
                        "Students",
                        "Visitors"
                );
                System.out.println(
                        "=============================================================="
                );
                // Display every classroom
                for (Classroom classroom : classrooms) {
                    System.out.println(
                            classroom.getStatus()
                    );
                }

                System.out.println(
                        "=============================================================="
                );

                // Update status every 2 seconds
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}