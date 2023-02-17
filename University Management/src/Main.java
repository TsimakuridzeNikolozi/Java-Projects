import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UniManagementImpl management = new UniManagementImpl();

    public static void main(String[] args) throws CourseNotFoundException {
        String command;
        System.out.println("\n---------------- START OF THE PROGRAM -----------------\n");
        CommandParser parser = new CommandParser();
        printMenu();
        program:
        while(true) {
            System.out.print("\nEnter your command: ");
            command = scanner.next();
            int parsedCommand = parser.parseCommand(command);
            if (parsedCommand == 0) break;
        }
    }

    static void printMenu() {
        System.out.println("Select the activity you want to perform (Write the corresponding command)");
        System.out.println("    createStudent <user_id> <userFirstName> <userLastName> <facNumber>");
        System.out.println("    createCourse <courseName>");
        System.out.println("    createLector <user_id> <userFirstName> <userLastname> <lectorType> (PROFESSOR, ASSISTANCE, DOCENT)");
        System.out.println("    assignStudentToCourse <student_id> <courseName>");
        System.out.println("    assignLectorToCourse <lector_id> <lectorType> <courseName>");
        System.out.println("    deleteCourse <courseName>");
        System.out.println("    removeStudentFromCourse <student_id> <courseName>");
        System.out.println("    removeLectorFromCourse <lector_id> <lectorType> <courseName>");
        System.out.println("    MENU: to show the available commands");
        System.out.println("    EXIT: to exit the program");
    }

    /* Gets all the information needed for creating a student and creates it
       Prints the results to the console
     */
    static void createStudent() {
        int id = scanner.nextInt();
        String firstName = scanner.next(), lastName = scanner.next(), facNumber = scanner.next();
        Student student = management.createStudent(id, firstName, lastName, facNumber);
        if (student != null) System.out.println("Created a student named \"" + firstName + " " + lastName + "\"");
        else System.out.println("Failed to create a student");
    }

    /* Gets all the information needed for creating a course and creates it
       Prints the results to the console
     */
    static void createCourse() {
        String courseName = scanner.next();
        Course course = management.createCourse(courseName);
        if (course != null) System.out.println("Created a course named \"" + courseName + "\"");
        else System.out.println("Failed to create a course");
    }

    /* Gets all the information needed for creating a lector and creates it
       Prints the results to the console
     */
    static void createLector() {
        int id = scanner.nextInt();
        String firstName = scanner.next(), lastName = scanner.next(), lectorType = scanner.next();
        Lector lector = management.createLector(id, firstName, lastName, lectorType);
        if (lector != null) System.out.println("Created a " + lector.type.toString().toLowerCase() +
                                               " named \"" + firstName + " " + lastName + "\"");
        else System.out.println("Failed to create a lector");
    }

    /* Gets all the information needed for assigning a student to a course and performs the action
       Prints the results to the console
     */
    static void assignStudentToCourse() {
        int id = scanner.nextInt();
        String courseName = scanner.next();
        boolean result = management.addStudentToCourse(id, courseName);
        if (result) System.out.println("Added a student with the id " + id + " to a course named \"" +
                                        courseName + "\"\n");
        else System.out.println("Failed to assign student to the course, make sure the parameters are correct");
    }

    /* Gets all the information needed for assigning a lector to a course and performs the action
       Prints the results to the console
     */
    static void assignLectorToCourse() {
        int lectorId = scanner.nextInt();
        String lectorType = scanner.next(), courseName = scanner.next();
        boolean result = management.assignLectorToCourse(lectorId, lectorType, courseName);
        if (result) System.out.println("Assigned a " + lectorType + " with the id " + lectorId +
                                       " to a course named \"" + courseName + "\"\n");
        else System.out.println("Failed to assign lector to the course, make sure the parameters are correct");
    }

    /* Gets the course name and deletes it
       Prints the results to the console
     */
    static void deleteCourse() throws CourseNotFoundException {
        String courseName = scanner.next();
        boolean result = management.deleteCourse(courseName);
        if (result) System.out.println("Deleted a course named \"" + courseName + "\"");
        else System.out.println("Failed to delete a course");
    }

    /* Gets all the information needed for removing a student from a course and performs the action
       Prints the results to the console
     */
    static void removeStudentFromCourse() {
        int studentId = scanner.nextInt();
        String courseName = scanner.next();
        boolean result = management.removeStudentFromCourse(studentId, courseName);
        if (result) System.out.println("Removed a student with the ID " + studentId +
                                       " from the course named \"" + courseName + "\"");
        else System.out.println("Failed to remove student from the course, make sure the parameters are correct");
    }

    /* Gets all the information needed for removing a lector from a course and performs the action
       Prints the results to the console
     */
    static void removeLectorFromCourse() {
        int lectorId = scanner.nextInt();
        String lectorType = scanner.next(), courseName = scanner.next();
        boolean result = management.removeLectorFromCourse(lectorId, lectorType, courseName);
        if (result) System.out.println("Removed a " + lectorType.toLowerCase() + " with the ID " + lectorId +
                                       " from the course named \"" + courseName + "\"");
        else System.out.println("Failed to remove the lector from the course.");
    }

    // Clears the input and prints the error message if the entered command is wrong
    static void errorMessage() {
        scanner.nextLine();
        System.out.println("Wrong command, Please try again. \n");
    }
}