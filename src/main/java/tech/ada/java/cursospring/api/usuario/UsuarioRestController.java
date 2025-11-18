package tech.ada.java.cursospring.api.usuario;

import java.time.LocalDate;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final UsuarioJpaRepository repository; // Injeção de dependência, não inicializada aqui, delegada ao Spring
    private final ModelMapper modelMapper;

    public UsuarioRestController(UsuarioJpaRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/dummy")
    public Usuario dummyUsuario() {
        return new Usuario(UUID.randomUUID(), "Joanna", "joanna@email.com", LocalDate.now());
    }

    @GetMapping
    public Page<Usuario> listarTodos(@org.springframework.lang.NonNull Pageable pageable) {
        return this.repository.findAll(pageable);
        // Usando o repositório para buscar todos os usuários do banco de dados,
        // método herdado de JpaRepository
    }

    @GetMapping("/{uuid}")
    public UsuarioDTO buscarPorUuidDTO(@PathVariable UUID uuid) {
        Usuario usuario = buscarPorUuid(uuid);
        return this.modelMapper.map(usuario, UsuarioDTO.class);
    }

    private Usuario buscarPorUuid(UUID uuid) {
        // Usando o repositório para buscar o usuário pelo UUID
        return this.repository.findByUuid(uuid)
                .orElseThrow(
                        () -> new NaoEncontradoException("Usuário não encontrado."));
    }

    @PostMapping
    public Usuario criarUsuario(@RequestBody @Valid UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUuid(dto.getUuid() != null ? dto.getUuid() : UUID.randomUUID());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setDob(LocalDate.parse(dto.getDob()));
        usuario.setId(null); // Garante que o banco vai gerar o id
        return this.repository.save(usuario);
    }

    @PostMapping("/create-dummy")
    public Usuario createDummy() {
        UsuarioDTO dummyDto = new UsuarioDTO();
        dummyDto.setUuid(UUID.randomUUID());
        dummyDto.setNome("Dummy");
        dummyDto.setEmail("dummy@example.com");
        dummyDto.setDob(LocalDate.now().minusYears(18).toString());
        return this.criarUsuario(dummyDto);
    }

    @PutMapping("/{uuid}")
    public Usuario atualizarUsuario(@PathVariable UUID uuid, @RequestBody @Valid Usuario usuarioNovo) {
        Usuario usuario = this.buscarPorUuid(uuid);
        usuarioNovo.setId(usuario.getId());
        return this.repository.save(usuarioNovo); // Atualizando o usuário no banco de dados com o método conhecido como
                                                  // upsert
    }

    @Transactional
    @PatchMapping("/{uuid}/alterar-nome")
    public Usuario alterarNome(@PathVariable UUID uuid, @RequestBody Usuario usuarioAlterado) {
        this.repository.updateNome(uuid, usuarioAlterado.getNome());
        return this.buscarPorUuid(uuid);
    }

    @Transactional // garante que a operação de delete seja executada em uma transação ACID
    @DeleteMapping("/{uuid}")
    public void deletarUsuario(@PathVariable UUID uuid) {
        this.repository.deleteByUuid(uuid);
    }
}
