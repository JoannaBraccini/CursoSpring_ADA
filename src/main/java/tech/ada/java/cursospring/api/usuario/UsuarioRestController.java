package tech.ada.java.cursospring.api.usuario;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Swagger é acessado em http://localhost:8080/swagger-ui.html

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioRestController {

    private final UsuarioService service;

    @GetMapping("/dummy")
    public Usuario dummyUsuario() {
        return new Usuario(UUID.randomUUID(), "Joanna", "joanna@email.com", LocalDate.now());
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

    @GetMapping
    public Page<UsuarioDTO> listarTodos(@NonNull Pageable pageable) {
        return this.service.listarTodos(pageable);
    }

    @GetMapping("/{uuid}")
    public UsuarioDTO buscarPorUuidDTO(@PathVariable UUID uuid) {
        return this.service.buscarPorUuidDTO(uuid);
    }

    // @GetMapping("/{uuid}")
    // public Usuario buscarPorUuid(@PathVariable UUID uuid) {
    // return this.service.buscarPorUuid(uuid);
    // }

    @PostMapping
    public Usuario criarUsuario(@RequestBody @Valid UsuarioDTO usuario) {
        return this.service.criarUsuario(usuario);
    }

    @PutMapping("/{uuid}")
    public Usuario atualizarUsuario(@PathVariable UUID uuid, @RequestBody @Valid Usuario usuario) {
        return this.service.atualizarUsuario(uuid, usuario);
    }

    @PatchMapping("/{uuid}/alterar-nome")
    public Usuario alterarNome(@PathVariable UUID uuid, @RequestBody Usuario usuario) {
        return this.service.alterarNome(uuid, usuario);
    }

    @DeleteMapping("/{uuid}")
    public String deletarUsuario(@PathVariable UUID uuid) {
        return this.service.deletarUsuario(uuid);
    }
}
