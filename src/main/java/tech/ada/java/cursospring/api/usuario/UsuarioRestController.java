package tech.ada.java.cursospring.api.usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import tech.ada.java.cursospring.api.exception.NaoEncontradoException;

// Swagger é acessado em http://localhost:8080/swagger-ui.html

@RestController
@RequestMapping("/usuarios")
public class UsuarioRestController {

    private final List<Usuario> usuarioList = new ArrayList<>();
    private final UsuarioJpaRepository repository; // Injeção de dependência, não inicializada aqui, delegada ao Spring

    public UsuarioRestController(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/dummy")
    public Usuario dummyUsuario() {
        return new Usuario(UUID.randomUUID(), "Joanna", "joanna@email.com", LocalDate.now());
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return this.repository.findAll(); // Usando o repositório para buscar todos os usuários do banco de dados,
                                          // método herdado de JpaRepository
    }

    @GetMapping("/{uuid}")
    public Usuario buscarPorUuid(@PathVariable UUID uuid) {
        // Usando o repositório para buscar o usuário pelo UUID
        return this.repository.findByUuid(uuid)
                .orElseThrow(
                        () -> new NaoEncontradoException("Usuário não encontrado."));
    }

    @SuppressWarnings("null")
    @PostMapping("/")
    public Usuario criarUsuario(@RequestBody @Valid Usuario usuario) {
        return this.repository.save(usuario); // Salvando o usuário no banco de dados
    }

    @PostMapping("/create-dummy")
    public Usuario createDummy() {
        Usuario dummy = new Usuario(UUID.randomUUID(), "Dummy", "dummy@example.com", LocalDate.now());
        return this.criarUsuario(dummy);
    }

    @PutMapping("/{uuid}")
    public Usuario atualizarUsuario(@PathVariable UUID uuid, @RequestBody @Valid Usuario usuarioNovo) {
        Usuario usuario = this.buscarPorUuid(uuid);
        usuarioNovo.setId(usuario.getId());
        return this.repository.save(usuarioNovo); // Atualizando o usuário no banco de dados com o método conhecido como
                                                  // upsert
    }

    @PatchMapping("/{uuid}/alterar-nome")
    public Usuario alterarNome(@PathVariable UUID uuid, @RequestBody Usuario usuarioAlterado) {
        Usuario usuario = this.buscarPorUuid(uuid);
        usuario.setNome(usuarioAlterado.getNome());
        this.usuarioList.set(this.usuarioList.indexOf(usuario), usuarioAlterado);
        return usuarioAlterado;
    }

    @Transactional // garante que a operação de delete seja executada em uma transação ACID
    @DeleteMapping("/{uuid}")
    public void deletarUsuario(@PathVariable UUID uuid) {
        this.repository.deleteByUuid(uuid);
    }
}
