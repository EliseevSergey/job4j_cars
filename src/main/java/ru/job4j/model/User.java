package ru.job4j.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auto_user")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    @Column (nullable = false, unique = true, length = 20)
    private String login;
    @Column (nullable = false)
    private String password;
    @Column (nullable = false, unique = false, length = 255)
    private String name;
    @Column (name = "user_zone", nullable = false, length = 50)
    private String timezone;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_user_id")
    private List<Post> posts = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", login='" + login + '\'' + '}';
    }
}
