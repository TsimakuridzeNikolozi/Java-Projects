public interface UniManagement {
    /**
     * Create a new course with name courseName and return it
     **
     @return new instance of course with the passed name if it's created or
      * null in another case. A course will be created only if already does not exist
      * another course with the same courseName
     */
    public Course createCourse(String courseName);



    /**
     * Delete a course with name courseName
     **
     @return <code>true</code> only in case the course with passed name was removed
     */
    public boolean deleteCourse(String courseName) throws CourseNotFoundException;



    /**
     * Create and return new instance of Student with the passed arguments and initial state
     of the student
     **
     @return new instance of a student identified with the passed ID, if already does not
     exist,
      * and the other arguments as initial state if it's created or
      * null in another case
     */
    public Student createStudent(int id, String firstName, String lastName, String
            facNumber);



    /**
     * Delete a student with the passed ID
     **
     @return <code>true</code> only in cae the student was removed
     */
    public boolean deleteStudent(int id) throws StudentNotFoundException ;



    /**
     * Create a new assistance in the university with the passed arguments as initial state
     **
     @return new created professor assistance identified with the passed ID, if already does
     not exist with that ID
     */
    public Lector createLector(int id, String firstName, String lastName, String lectorType);



    /**
     * Delete the professor assistance with the passed ID, if such exists
     **
     @return <code>true</code> ONLY in case the assistance was removed
     */
    public boolean deleteLector(int id, String type);



    /**
     * Assign a professor to a course
     **
     @return <code>true</code> ONLY n case the professor was successfully assigned to
     the course
     */
    public boolean assignLectorToCourse(int lectorId, String lectorType, String courseName);



    /**
     * Add a student to a course
     **
     @return <code.true</code> ONLY inca se the student is successfully added to the
     course
     */
    public boolean addStudentToCourse(int studentId, String courseName);



    /**
     * Add all students to a course
     **
     @return <code>true</code> ONLY inc ase all students are added to a course
     */
    public boolean addStudentsToCourse(Student[] students, Course course);

    /**
     * Remove a student from a course
     **
     @return <code>true</code> only in case the student was successfully removed from a
     course
     */
    public boolean removeStudentFromCourse(int studentId, String courseName);



    /**
     * Remove a lector from a course
     **
     @return <code>true</code> only in case the lector was successfully removed from a
     course
     */
    public boolean removeLectorFromCourse(int lectorId, String lectorType, String courseName);
}
