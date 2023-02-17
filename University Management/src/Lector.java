import java.util.ArrayList;
import java.util.List;

public class Lector extends User {
    enum LectorType {
        DOCENT,
        PROFESSOR,
        ASSISTANCE
    }

    LectorType type;
    private List<Course> courses;

    Lector(int id, String firstName, String lastName, String type) {
        super(id, firstName, lastName);
        courses = new ArrayList<>();
        switch (type) {
            case "PROFESSOR" -> this.type = LectorType.PROFESSOR;
            case "ASSISTANCE" -> this.type = LectorType.ASSISTANCE;
            case "DOCENT" -> this.type = LectorType.DOCENT;
            default -> this.type = null;
        }
    }

    public void addCourse(Course course) {
        if (courses.size() == 4) {
            System.out.println("Can't teach more than 4 courses.");
            return;
        }
        courses.add(course);
    }

    public boolean removeCourse(Course course) {
        for(int i = 0; i < courses.size(); i++){
            if (courses.get(i).name.equals(course.name)){
                courses.remove(i);
                return true;
            }
        }
        System.out.println("Couldn't find a course named " + course.name + " in the lectors courses.");
        return false;
    }

    public List<Course> getCourses() {
        return this.courses;
    }
}
