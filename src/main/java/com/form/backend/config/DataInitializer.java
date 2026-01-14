package com.form.backend.config;

import com.form.backend.model.Usuario;
import com.form.backend.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Inyectamos los valores desde application.properties
    @Value("${flavis.admin1.email}")
    private String admin1Email;

    @Value("${flavis.admin1.password}")
    private String admin1Pass;

    @Value("${flavis.admin2.email}")
    private String admin2Email;

    @Value("${flavis.admin2.password}")
    private String admin2Pass;

    @Override
    public void run(String... args) throws Exception {
        // Crear Admin 1 si no existe
        crearAdminSiNoExiste(admin1Email, admin1Pass);

        // Crear Admin 2 si no existe
        crearAdminSiNoExiste(admin2Email, admin2Pass);
    }

    private void crearAdminSiNoExiste(String email, String password) {
        if (usuarioRepository.findByEmail(email).isEmpty()) {
            Usuario nuevoAdmin = new Usuario();
            nuevoAdmin.setEmail(email);
            nuevoAdmin.setPassword(password);
            nuevoAdmin.setRol("ROLE_ADMIN"); // AGREGA EL PREFIJO ROLE_
            usuarioRepository.save(nuevoAdmin);
        }
    }
}