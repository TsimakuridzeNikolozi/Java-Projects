public class CommandParser {
    private final String command1 = "createStudent";
    private final String command2 = "createCourse";
    private final String command3 = "createLector";
    private final String command4 = "assignStudentToCourse";
    private final String command5 = "assignLectorToCourse";
    private final String command6 = "deleteCourse";
    private final String command7 = "removeStudentFromCourse";
    private final String command8 = "removeLectorFromCourse";
    private final String command9 = "MENU";
    private final String command10 = "EXIT";


    public int parseCommand(String argument) throws CourseNotFoundException {
        switch (argument) {
            case command1 -> Main.createStudent();
            case command2 -> Main.createCourse();
            case command3 -> Main.createLector();
            case command4 -> Main.assignStudentToCourse();
            case command5 -> Main.assignLectorToCourse();
            case command6 -> Main.deleteCourse();
            case command7 -> Main.removeStudentFromCourse();
            case command8 -> Main.removeLectorFromCourse();
            case command9 -> Main.printMenu();
            case command10 -> {
                return 0;
            }
            default -> Main.errorMessage();
        };
        return -1;
    }
}
