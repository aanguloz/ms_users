package com.user.project.ms_user.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "navigate")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Column(unique = true)
    private String name;

    private String description;
    private Boolean isDeleted = false;

    @ManyToMany(mappedBy = "modules") // <-- Relación inversa
    private Set<Role> roles = new HashSet<>();

    public Module(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
}
