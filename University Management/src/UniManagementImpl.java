import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class UniManagementImpl implements UniManagement{
    private final int maxStudents = 1000, maxCourses = 10;
    private int studentCount;

    private Student[] students;
    private List<Course> courses;
    private List<Lector> lectors;

    UniManagementImpl(){
        students = new Student[maxStudents];
        courses = new ArrayList<>(maxCourses);
        lectors = new ArrayList<>();
    }

    // Predicate for checking if the course with the passed name exists in the list of courses
    private final Predicate<String> checkCourse = (String courseName) ->
            { for(Course c: courses) {if (c != null && c.name.equals(courseName)) return true;} return false; };

    // Predicate for checking if the student with the passed ID exists in the array of students
    private final Predicate<Integer> checkStudent = (Integer id) ->
            { for(Student s: students) {if (s != null && s.id == id) return true;} return false; };

    @Override
    public Course createCourse(String courseName) {
        if (checkCourse.test(courseName)) {
            System.out.println("A course with the name \"" + courseName + "\" already exists.");
            return null;
        }
        if (courses.size() == maxCourses) {
            System.out.println("There are already 10 courses in the university, can't create more");
            return null;
        }

        Course course = new Course(courseName);
        courses.add(course);
        return course;
    }

    @Override
    public boolean deleteCourse(String courseName) throws CourseNotFoundException {
        if (!checkCourse.test(courseName))
            throw(new CourseNotFoundException("Course named \"" + courseName + "\" couldn't be found"));

        int courseInd = getCourseIndex(courseName);
        Course course = courses.get(courseInd);

        courses.remove(course);
        return true;
    }

    @Override
    public Student createStudent(int id, String firstName, String lastName, String facNumber) {
        if (studentCount == 1000){
            System.out.println("University is full, can't take new students.");
            return null;
        }
        if (checkStudent.test(id)) {
            System.out.println("A student with the id \"" + id + "\" already exists.");
            return null;
        }

        Student student = new Student(id, firstName, lastName, facNumber);
        students[studentCount++] = student;
        return student;
    }

    @Override
    public boolean deleteStudent(int id) throws StudentNotFoundException {
        int studentInd = getStudentIndex(id);
        if (studentInd == -1) throw(new StudentNotFoundException("Student couldn't be found"));

        Student student = students[studentInd];

        // Remove student from all the courses
        for(Course c1: student.getCourses()){
            for(Course c2: courses) {
                if (c1 == c2) c2.removeStudent(student);
            }
        }

        Student[] temp = new Student[1000];
        System.arraycopy(students, 0, temp, 0, studentInd);
        System.arraycopy(students, studentInd + 1, temp, studentInd, maxStudents - studentInd + 1);
        students = temp;
        studentCount--;

        System.out.println("Deleted a student named \"" + student.getFirstName() +
                            " " + student.getLastName() + "\" with the id " + id);
        return true;
    }

    @Override
    public Lector createLector(int id, String firstName, String lastName, String lectorType) {
        if (getLectorIndex(id, lectorType) != -1) {
            System.out.println("A person with the id \"" + id + "\" already exists.");
            return null;
        }

        if (!lectorType.equals("PROFESSOR") && !lectorType.equals("ASSISTANCE") && !lectorType.equals("DOCENT")){
            System.out.println("Wrong lector type, please type in one of the following (PROFESSOR, ASSISTANCE, DOCENT)");
            return null;
        }

        Lector lector = new Lector(id, firstName, lastName, lectorType);
        lectors.add(lector);
        return lector;
    }

    @Override
    public boolean deleteLector(int id, String type) {
        int lectorIndex = getLectorIndex(id, type);
        if (lectorIndex == -1) {
            System.out.println("Couldn't find the lector with the ID " + id + " and type \"" + type + "\"");
            return false;
        }

        Lector lector = lectors.get(lectorIndex);

        // Remove Lector from all the courses
        for(Course c1: lector.getCourses()){
            for(Course c2: courses) {
                if (c1 == c2) c2.removeLector(lector);
            }
        }

        lectors.remove(lectorIndex);
        return true;
    }

    @Override
    public boolean assignLectorToCourse(int lectorId, String lectorType, String courseName) {
        int lectorInd = getLectorIndex(lectorId, lectorType), courseInd = getCourseIndex(courseName);
        if (lectorInd == -1 || courseInd == -1) return false;

        lectors.get(lectorInd).addCourse(courses.get(courseInd));
        courses.get(courseInd).setLector(lectors.get(lectorInd));
        return true;
    }


    @Override
    public boolean addStudentToCourse(int studentId, String courseName) {
        if (!checkCourse.test(courseName) || !checkStudent.test(studentId)) return false;

        int studentInd = getStudentIndex(studentId), courseInd = getCourseIndex(courseName);

        students[studentInd].addCourse(courses.get(courseInd));
        courses.get(courseInd).addStudent(students[studentInd]);
        return true;
    }

    @Override
    public boolean addStudentsToCourse(Student[] students, Course course) {
        if (!courses.contains(course)) return false;

        for(Student s: students) {
            if (!checkStudent.test(s.id)) return false;
        }

        for (Student s: this.students) {
            for(Student s1: students) {
                if (s == s1) {
                    s.addCourse(course);
                    s1.addCourse(course);
                }
            }
        }

        for(Course c: courses) {
            if (c == course) {
                for (Student s: students) {
                    c.addStudent(s);
                }
            }
        }

        return true;
    }

    @Override
    public boolean removeStudentFromCourse(int studentId, String courseName) {
        if (!checkCourse.test(courseName) || !checkStudent.test(studentId)) return false;

        int studentInd = getStudentIndex(studentId), courseInd = getCourseIndex(courseName);

        boolean result1 = students[studentInd].removeCourse(courses.get(courseInd));
        boolean result2 = courses.get(courseInd).removeStudent(students[studentInd]);

        return result1 && result2;
    }

    @Override
    public boolean removeLectorFromCourse(int lectorId, String lectorType, String courseName) {
        int lectorInd = getLectorIndex(lectorId, lectorType), courseInd = getCourseIndex(courseName);
        if (lectorInd == -1 || courseInd == -1) return false;

        boolean result1 = lectors.get(lectorInd).removeCourse(courses.get(courseInd));
        boolean result2 = courses.get(courseInd).removeLector(lectors.get(lectorInd));

        return result1 && result2;
    }

    // Returns the index of a student with the passed id from the array of students or -1 if not found
    private int getStudentIndex(int studentId) {
        for(int i = 0; i < studentCount; i++) {
            if (students[i].id == studentId) return i;
        }
        return -1;
    }

    // Returns the index of a course with the passed course name from the list of courses or -1 if not found
    private int getCourseIndex(String courseName) {
        for(int i = 0; i < courses.size(); i++) {
            if (courses.get(i).name.equals(courseName)) return i;
        }
        return -1;
    }

    // Returns the index of a lector with the passed parameters from the list of lectors or -1 if not found
    private int getLectorIndex(int lectorId, String lectorType) {
        for(int i = 0; i < lectors.size(); i++) {
            Lector lector = lectors.get(i);
            if (lector.id == lectorId && lector.type.toString().equals(lectorType))
                return i;
        }
        return -1;
    }
}
