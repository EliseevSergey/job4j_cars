package ru.job4j.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auto_user")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private String login;
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_user_id")
    private List<Post> posts = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", login='" + login + '\'' + '}';
    }
}
