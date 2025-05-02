package hkmu.wadd.model;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int lectureId;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Comment() {
    }

    public Comment(int lectureId, User user, String content) {
        this.lectureId = lectureId;
        this.user = user;
        this.content = content;
        this.timestamp = new Date();
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public int getLectureId() {
        return lectureId;
    }
    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
