import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private String facNumber;
    private List<Course> courses;

    Student(int id, String firstName, String lastName, String facNumber) {
        super(id, firstName, lastName);
        this.facNumber = facNumber;
        courses = new ArrayList<>();
    }

    public void addCourse(Course course) {
        if (courses.size() == 10){
            System.out.println("Can't enroll in more than 10 courses.");
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
        System.out.println("Couldn't find a course named " + course.name + " in the students courses.");
        return false;
    }

    public List<Course> getCourses() {
        return this.courses;
    }
}
