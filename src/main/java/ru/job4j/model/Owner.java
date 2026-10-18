package ru.job4j.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Entity
@Table (name = "owner")
@AllArgsConstructor
@NoArgsConstructor

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
