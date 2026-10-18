package ru.job4j.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Entity
@Table(name = "engine")
@AllArgsConstructor
@NoArgsConstructor

public class Engine {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Double volume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuel_id",  nullable = false)
    private Fuel fuel;

    @Column(name = "horse_power", nullable = false)
    private Integer horsePower;
}
