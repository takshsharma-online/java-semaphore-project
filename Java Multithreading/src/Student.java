public class Student extends Thread{
    private Classroom classroom;//The Classroom assigned to the student.

    //Following is the constructor
    public Student(String name, Classroom classroom){
        super(name);
        this.classroom = classroom;
    }
    //Thread Execution Starts here
    @Override
    public void run(){
        try {

            // Student enters classroom
            classroom.studentEnter();
            // Student waits until lecturer finishes
            classroom.waitForLecture();
            // Student leaves classroom
            classroom.studentLeave();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(
                    getName() + " was interrupted."
            );
        }
    }
}
