package edu.utvt.extraordinario.data.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("1")
@Table(name = "tb_usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoEntity extends UsuarioEntity {

    @Column(nullable = false)
    private Integer edad;

    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @Column(nullable = false, length = 100)
    private String carrera;

    @Column(nullable = false, length = 10)
    private String grado;

    @Column(nullable = false, length = 20)
    private String grupo;

    @Column(nullable = false)
    private Integer semestre;
}

