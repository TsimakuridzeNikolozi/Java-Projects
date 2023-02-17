import java.util.ArrayList;
import java.util.List;

public class Course {
    String name; // Unique
    private List<Student> students; // Max 30
    private Lector assistance;
    private Lector lector;

    // Constructor 1
    Course(String name) {
        this.name = name;
        this.students = new ArrayList<>();
    }

    // Constructor 2
    Course(String name, Lector assistance, Lector lector) {
        this.name = name;
        this.students = new ArrayList<>();
        this.assistance = assistance;
        this.lector = lector;
    }

    // Method for adding a student to the course
    public void addStudent(Student student) {
        if (this.students.size() == 30) {
            System.out.println("The course is full, can't add new students.");
            return;
        }
        this.students.add(student);
    }

    // Method for deleting a student from the course
    public boolean removeStudent(Student student) {
        for(int i = 0; i < students.size(); i++) {
            if (students.get(i).id == student.id) {
                students.remove(i);
                return true;
            }
        }
        System.out.println("Couldn't find a student with the ID " + student.getId() + " on the course.");
        return false;
    }

    // Method for setting the lecturer for the course
    public void setLector(Lector lector) {
        if (lector.type != null && lector.type.toString().equals("ASSISTANCE")) this.assistance = lector;
        else this.lector = lector;
    }

    // Method for removing the lector from the course
    public boolean removeLector(Lector lector) {
        if (lector == null) return false;
        if (lector.type.toString().equals("ASSISTANCE")){
            if (this.assistance == null) return false;
            if (lector.id != this.assistance.id || lector.type != this.assistance.type) return false;
            this.assistance = null;
        }
        else {
            if (this.lector == null) return false;
            if (lector.id != this.lector.id || lector.type != this.lector.type) return false;
            this.lector = null;
        }

        return true;
    }

    // getters
    public List<Student> getStudents() {
        return this.students;
    }

    public Lector getLector(String lectorType) {
        if (lectorType.equals("ASSISTANCE")) return assistance;
        else return lector;
    }
}
