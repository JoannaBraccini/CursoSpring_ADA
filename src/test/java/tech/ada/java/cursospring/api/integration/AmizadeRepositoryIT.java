package tech.ada.java.cursospring.api.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.ActiveProfiles;

import tech.ada.java.cursospring.api.amizade.Amizade;
import tech.ada.java.cursospring.api.amizade.AmizadeRepository;
import tech.ada.java.cursospring.api.usuario.Usuario;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql("/schema.sql")
@ActiveProfiles("postgres")
public class AmizadeRepositoryIT {

    @Autowired
    private AmizadeRepository amizadeRepository;

    @Autowired
    private tech.ada.java.cursospring.api.usuario.UsuarioRepository usuarioRepository;

    @Test
    void saveAndFind() {
        UUID uuidA = UUID.randomUUID();
        UUID uuidB = UUID.randomUUID();
        Usuario a = new Usuario(uuidA, "Usuario A", "a-it@example.com", LocalDate.of(1990, 1, 1));
        Usuario b = new Usuario(uuidB, "Usuario B", "b-it@example.com", LocalDate.of(1991, 2, 2));

        usuarioRepository.save(a);
        usuarioRepository.save(b);

        Amizade amizade = new Amizade(a, b);
        amizadeRepository.save(amizade);

        Long amizadeId = amizade.getId();
        assertTrue(amizadeId != null, "Amizade ID should not be null");
        Optional<Amizade> found = amizadeRepository.findById(amizadeId);
        assertTrue(found.isPresent());
    }
}
