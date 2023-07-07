package de.marczewski.springdatarest.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

// Schema is needed for PostgreSQL
@Table(name = "company" , schema = "public")
@Entity(name = "company")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "company")
    private List<Employee> employees;
}
