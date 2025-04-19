package edu.utvt.extraordinario.data.controllers;

import edu.utvt.extraordinario.data.entities.AlumnoEntity;
import edu.utvt.extraordinario.data.services.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    private Map<String, String> jsonMessage(String key, String message) {
        return Collections.singletonMap(key, message);
    }

    // Crear un alumno
    @PostMapping
    public ResponseEntity<Object> createAlumno(@RequestBody AlumnoEntity alumno) {
        if (alumno.getNombre() == null || alumno.getNombre().isEmpty()) {
            return new ResponseEntity<>(jsonMessage("error", "El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (alumno.getApellido() == null || alumno.getApellido().isEmpty()) {
            return new ResponseEntity<>(jsonMessage("error", "El apellido es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (alumno.getEmail() == null || alumno.getEmail().isEmpty()) {
            return new ResponseEntity<>(jsonMessage("error", "El correo electrónico es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (alumno.getEdad() == null) {
            return new ResponseEntity<>(jsonMessage("error", "La edad es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if (alumno.getFechaNacimiento() == null) {
            return new ResponseEntity<>(jsonMessage("error", "La fecha de nacimiento es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        if (alumno.getGrupo() == null) {
            return new ResponseEntity<>(jsonMessage("error", "El grupo es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        if (alumno.getGrado() == null) {
            return new ResponseEntity<>(jsonMessage("error", "El grado es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (alumno.getSemestre() == null) {
            return new ResponseEntity<>(jsonMessage("error", "El semestre es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (alumno.getMatricula() == null || alumno.getMatricula().isEmpty()) {
            return new ResponseEntity<>(jsonMessage("error", "La matrícula es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if (alumno.getCarrera() == null || alumno.getCarrera().isEmpty()) {
            return new ResponseEntity<>(jsonMessage("error", "La carrera es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        Optional<AlumnoEntity> existingAlumno = alumnoService.findByEmail(alumno.getEmail());
        if (existingAlumno.isPresent()) {
            return new ResponseEntity<>(jsonMessage("error", "El correo electrónico ya está registrado"), HttpStatus.CONFLICT);
        }

        AlumnoEntity savedAlumno = alumnoService.save(alumno);
        return new ResponseEntity<>(savedAlumno, HttpStatus.CREATED);
    }

    // Listar todos los alumnos
    @GetMapping
    public ResponseEntity<Object> getAllAlumnos() {
        List<AlumnoEntity> alumnos = alumnoService.findAll();
        if (alumnos.isEmpty()) {
            return new ResponseEntity<>(jsonMessage("message", "No se encontraron alumnos"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }

    // Obtener un alumno por ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getAlumnoById(@PathVariable UUID id) {
        Optional<AlumnoEntity> alumno = alumnoService.findById(id);
        return alumno
                .<ResponseEntity<Object>>map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(jsonMessage("error", "Alumno no encontrado"), HttpStatus.NOT_FOUND));
    }

    // Actualizar un alumno
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAlumno(@PathVariable UUID id, @RequestBody AlumnoEntity alumno) {
        Optional<AlumnoEntity> existingAlumnoOpt = alumnoService.findById(id);

        if (!existingAlumnoOpt.isPresent()) {
            return new ResponseEntity<>(jsonMessage("error", "Alumno no encontrado"), HttpStatus.NOT_FOUND);
        }

        AlumnoEntity existingAlumno = existingAlumnoOpt.get();
        Map<String, Object> updatedFields = new LinkedHashMap<>();

        // Nombre
        if (alumno.getNombre() != null && !alumno.getNombre().isEmpty()) {
            existingAlumno.setNombre(alumno.getNombre());
            updatedFields.put("nombre", alumno.getNombre());
        }

        // Apellido
        if (alumno.getApellido() != null && !alumno.getApellido().isEmpty()) {
            existingAlumno.setApellido(alumno.getApellido());
            updatedFields.put("apellido", alumno.getApellido());
        }

        // Email (verifica que no lo tenga otro alumno)
        if (alumno.getEmail() != null && !alumno.getEmail().isEmpty()) {
            Optional<AlumnoEntity> alumnoWithEmail = alumnoService.findByEmail(alumno.getEmail());
            if (alumnoWithEmail.isPresent() && !alumnoWithEmail.get().getId().equals(id)) {
                return new ResponseEntity<>(jsonMessage("error", "El correo electrónico ya está registrado para otro alumno"), HttpStatus.CONFLICT);
            }
            existingAlumno.setEmail(alumno.getEmail());
            updatedFields.put("email", alumno.getEmail());
        }

        // Edad
        if (alumno.getEdad() != null) {
            existingAlumno.setEdad(alumno.getEdad());
            updatedFields.put("edad", alumno.getEdad());
        }

        // Fecha de nacimiento
        if (alumno.getFechaNacimiento() != null) {
            existingAlumno.setFechaNacimiento(alumno.getFechaNacimiento());
            updatedFields.put("fechaNacimiento", alumno.getFechaNacimiento());
        }

        // Matrícula
        if (alumno.getMatricula() != null && !alumno.getMatricula().isEmpty()) {
            existingAlumno.setMatricula(alumno.getMatricula());
            updatedFields.put("matricula", alumno.getMatricula());
        }

        // Carrera
        if (alumno.getCarrera() != null && !alumno.getCarrera().isEmpty()) {
            existingAlumno.setCarrera(alumno.getCarrera());
            updatedFields.put("carrera", alumno.getCarrera());
        }

        if (updatedFields.isEmpty()) {
            return new ResponseEntity<>(jsonMessage("error", "No se proporcionó ningún campo válido para actualizar"), HttpStatus.BAD_REQUEST);
        }

        alumnoService.save(existingAlumno);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Alumno actualizado");
        response.put("actualizado", updatedFields);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Eliminar un alumno
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAlumno(@PathVariable UUID id) {
        Optional<AlumnoEntity> alumno = alumnoService.findById(id);
        if (alumno.isPresent()) {
            alumnoService.deleteById(id);
            return new ResponseEntity<>(jsonMessage("message", "Alumno eliminado con éxito"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(jsonMessage("error", "Alumno no encontrado"), HttpStatus.NOT_FOUND);
        }
    }
}
