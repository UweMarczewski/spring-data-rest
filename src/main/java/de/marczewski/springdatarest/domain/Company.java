package de.marczewski.springdatarest.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

// Schema is needed for PostgreSQL
@Table(name = "company", schema = "public")
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
    @NotBlank
    private String name;

    @Column(name = "number_of_employees")
    @Min(1)
    private Integer numberOfEmployees;

    @OneToMany(mappedBy = "company")
    private List<Employee> employees;
}
