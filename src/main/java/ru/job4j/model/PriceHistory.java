package ru.job4j.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "price_history")
@AllArgsConstructor
@NoArgsConstructor
public class PriceHistory {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private Long before;
    private Long after;
    private LocalDateTime created = LocalDateTime.now();
}
