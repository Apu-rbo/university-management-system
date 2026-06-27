package university.core;

import university.model.Course;

/**
 * Abstraction: defines the contract for anything that can be enrolled
 * in courses, without saying anything about HOW enrollment is stored.
 */
public interface Enrollable {
    void enroll(Course course);
    void dropCourse(Course course);
}
