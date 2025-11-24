package tech.ada.java.cursospring.api.integration;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.ActiveProfiles;

import tech.ada.java.cursospring.api.usuario.Usuario;
import tech.ada.java.cursospring.api.usuario.UsuarioDTO;
import tech.ada.java.cursospring.api.usuario.UsuarioRepository;
import tech.ada.java.cursospring.api.usuario.UsuarioService;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import tech.ada.java.cursospring.api.exception.NaoEncontradoException;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql("/schema.sql")
@ActiveProfiles("postgres")
public class UsuarioRepositoryIT {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void saveAndFindByUuid() {
        UUID uuid = UUID.randomUUID();
        Usuario u = new Usuario(uuid, "Teste IT", "it@example.com", LocalDate.of(1990, 1, 1));
        usuarioRepository.save(u);

        Optional<Usuario> found = usuarioRepository.findByUuid(uuid);
        assertTrue(found.isPresent());
        assertEquals("Teste IT", found.get().getNome());
    }

    @Test
    @DisplayName("Buscar por UUID - sucesso")
    void buscarPorUuid() {
        // Criar e persistir um usuário com UUID fixo, então buscar via serviço
        UUID uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        Usuario seeded = new Usuario(uuid, "Seeded User", "seed@example.com", LocalDate.of(1995, 5, 5));
        usuarioRepository.save(seeded);

        UsuarioDTO usuario = this.usuarioService.buscarPorUuidDTO(uuid);
        assertNotNull(usuario);
        assertEquals("Seeded User", usuario.getNome());
        assertEquals("seed@example.com", usuario.getEmail());
    }

    @Test
    @DisplayName("Buscar por UUID - não encontrado")
    void buscarPorUuidNaoEncontrado_deveLancarNaoEncontradoException() {
        UUID uuidNaoExistente = UUID.randomUUID();

        Assertions.assertThrows(
                NaoEncontradoException.class,
                () -> this.usuarioService.buscarPorUuidDTO(uuidNaoExistente));
    }
}
