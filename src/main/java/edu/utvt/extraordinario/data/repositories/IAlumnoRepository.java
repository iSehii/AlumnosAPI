package edu.utvt.extraordinario.data.repositories;

import edu.utvt.extraordinario.data.entities.AlumnoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAlumnoRepository extends JpaRepository<AlumnoEntity, UUID> {

    // Metodo para verificar si un alumno con el mismo email ya existe
    boolean existsByEmail(String email);

    // JPA Query Methods
    List<AlumnoEntity> findByNombre(String nombre);

    List<AlumnoEntity> findByApellido(String apellido);
    Optional<AlumnoEntity> findByEmail(String email);

    List<AlumnoEntity> findByNombreContaining(String nombre);

    List<AlumnoEntity> findByApellidoContaining(String apellido);

    Page<AlumnoEntity> findByGrado(String grado, Pageable pageable);

}
