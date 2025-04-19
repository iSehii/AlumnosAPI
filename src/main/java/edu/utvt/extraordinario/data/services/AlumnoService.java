package edu.utvt.extraordinario.data.services;

import edu.utvt.extraordinario.data.entities.AlumnoEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlumnoService {

    // Guardar un nuevo alumno
    public AlumnoEntity save(AlumnoEntity alumno);

    // Actualizar un alumno existente
    public AlumnoEntity update(AlumnoEntity alumno, UUID alumnoId);

    // Encontrar un alumno por ID
    public Optional<AlumnoEntity> findById(UUID alumnoId);

    // Obtener todos los alumnos
    public List<AlumnoEntity> findAll();

    // Eliminar un alumno por ID
    public void deleteById(UUID alumnoId);

    // Obtener una lista de alumnos con paginación
    public Page<AlumnoEntity> findAll(Integer page, Integer pageSize);

    public Optional<AlumnoEntity> findByEmail(String email);
}
