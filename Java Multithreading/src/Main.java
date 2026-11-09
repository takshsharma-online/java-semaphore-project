public static void main(String[] args) throws InterruptedException {

    // 1. CREATE CLASSROOMS
    // Each classroom has:
    // Name, maximum capacity, expected number of students
    Classroom[] classrooms = {
            new Classroom("C003", 60, 15),
            new Classroom("C207", 60, 15),
            new Classroom("105", 20, 10),
            new Classroom("101", 30, 12)
    };
    // 2. CREATE STUDENT THREADS
    List<Student> students = new ArrayList<>();
    int studentNumber = 1;
    // Expected number of students in each classroom
    int[] studentCounts = {15, 15, 10, 12};
    // Loop through all classrooms
    for (int i = 0; i < classrooms.length; i++) {
        Classroom classroom = classrooms[i];
        // Create students for the current classroom
        for (int j = 0; j < studentCounts[i]; j++) {
            Student student = new Student(
                    "Student-" + studentNumber,
                    classroom
            );
            students.add(student);
            studentNumber++;
        }
    }
    // 3. CREATE VISITOR THREADS
    List<Visitor> visitors = new ArrayList<>();
    int visitorNumber = 1;
    // Create two visitors for each classroom
    for (Classroom classroom : classrooms) {
        for (int i = 0; i < 2; i++) {
            Visitor visitor = new Visitor(
                    "Visitor-" + visitorNumber,
                    classroom
            );
            visitors.add(visitor);
            visitorNumber++;
        }
    }
    // 4. CREATE LECTURER THREADS
    // Names of the six lecturers
    String[] lecturerNames = {
            "Martin",
            "Fergal",
            "Muhammed",
            "Atif",
            "Rabbiya",
            "Ciaran"
    };
    List<Lecturer> lecturers = new ArrayList<>();
    // Create lecturer threads
    for (String name : lecturerNames) {
        Lecturer lecturer = new Lecturer(
                name,
                classrooms
        );
        lecturers.add(lecturer);
    }
    // 5. CREATE MONITOR THREAD

    // Monitor displays classroom information
    // every two seconds

    Monitor monitor = new Monitor(classrooms);

    // 6. START MONITOR

    System.out.println("College Simulation Starting...");

    monitor.start();

    // 7. START STUDENT THREADS
    System.out.println("Starting students...");

    for (Student student : students) {

        student.start();
    }
    // 8. START VISITOR THREADS


    System.out.println("Starting visitors...");

    for (Visitor visitor : visitors) {

        visitor.start();
    }
    // 9. START LECTURER THREADS


    System.out.println("Starting lecturers...");

    for (Lecturer lecturer : lecturers) {

        lecturer.start();
    }
    // 10. WAIT FOR STUDENTS TO FINISH

    // Main thread waits until all students finish

    for (Student student : students) {
        student.join();
    }
    // 11. WAIT FOR VISITORS TO FINISH

    // Main thread waits until all visitors finish

    for (Visitor visitor : visitors) {
        visitor.join();
    }
    // 12. WAIT FOR LECTURERS TO FINISH
    // Main thread waits until all lecturers finish
    for (Lecturer lecturer : lecturers) {
        lecturer.join();
    }
    // 13. STOP MONITOR THREAD
    // Interrupt monitor after simulation finishes
    monitor.interrupt();
    monitor.join();
    // 14. SIMULATION COMPLETED
    System.out.println("\nCollege simulation completed successfully!");
}