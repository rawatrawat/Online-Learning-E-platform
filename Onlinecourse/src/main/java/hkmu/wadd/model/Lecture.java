package hkmu.wadd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Lecture {
    @Id
    private Integer id;
    private String topic;
    private String lectureFileName;
    private String exerciseFileName;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    public String getTopic() {
        return topic;
    }

    public String getLectureFileName() {
        return lectureFileName;
    }

    public void setLectureFileName(String lectureFileName) {
        this.lectureFileName = lectureFileName;
    }

    public String getExerciseFileName() {
        return exerciseFileName;
    }

    public void setExerciseFileName(String exerciseFileName) {
        this.exerciseFileName = exerciseFileName;
    }
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
