package edu.utvt.extraordinario.data.services;

import edu.utvt.extraordinario.data.entities.AlumnoEntity;
import edu.utvt.extraordinario.data.repositories.IAlumnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlumnoServiceImplementation implements AlumnoService {

    private final IAlumnoRepository alumnoRepository;

    // Guardar un nuevo alumno
    @Override
    public AlumnoEntity save(AlumnoEntity alumno) {
        return alumnoRepository.save(alumno);
    }

    // Actualizar un alumno existente
    @Override
    public AlumnoEntity update(AlumnoEntity alumno, UUID alumnoId) {
        Optional<AlumnoEntity> alumnoOptional = this.alumnoRepository.findById(alumnoId);

        if (alumnoOptional.isPresent()) {
            AlumnoEntity existingAlumno = alumnoOptional.get();

            // Actualiza los datos del alumno
            existingAlumno.setNombre(alumno.getNombre());
            existingAlumno.setApellido(alumno.getApellido());
            existingAlumno.setEmail(alumno.getEmail());
            existingAlumno.setFechaNacimiento(alumno.getFechaNacimiento());
            existingAlumno.setMatricula(alumno.getMatricula());
            existingAlumno.setGrado(alumno.getGrado());

            // Guarda los cambios
            return this.alumnoRepository.save(existingAlumno); // Retorna el alumno actualizado
        } else {
            throw new IllegalArgumentException("Alumno no encontrado con ID: " + alumnoId);
        }
    }

    // Buscar un alumno por ID
    @Override
    public Optional<AlumnoEntity> findById(UUID alumnoId) {
        return alumnoRepository.findById(alumnoId);
    }

    // Buscar un alumno por email (retorna Optional en lugar de lista)
    @Override
    public Optional<AlumnoEntity> findByEmail(String alumnoEmail) {
        return alumnoRepository.findByEmail(alumnoEmail);
    }

    // Obtener todos los alumnos
    @Override
    public List<AlumnoEntity> findAll() {
        return alumnoRepository.findAll();
    }

    // Eliminar un alumno por ID
    @Override
    public void deleteById(UUID alumnoId) {
        if (alumnoRepository.existsById(alumnoId)) {
            alumnoRepository.deleteById(alumnoId);
        } else {
            throw new IllegalArgumentException("Alumno no encontrado");
        }
    }

    // Obtener alumnos con paginación
    @Override
    public Page<AlumnoEntity> findAll(Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return alumnoRepository.findAll(pageRequest);
    }
}
