package ru.job4j.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

@Data
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Entity
@Table (name = "owner")

public class Owner {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    @Column (nullable = false)
    private String name;
    @OneToOne
    @JoinColumn (name = "user_id", nullable = false, unique = true)
    private User user;
}
