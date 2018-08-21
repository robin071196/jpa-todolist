package com.example.postgresdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "todo")
public class ToDo extends AuditModel {
    @Id
    @GeneratedValue(generator = "todo_generator")
    @SequenceGenerator(
            name = "todo_generator",
            sequenceName = "todo_sequence",
            initialValue = 1000
    )
    private Long id;
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    private String title;
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    private String description;
    public String getDescription() {return description;}
    public void setDescription(String description) { this.description = description; }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
