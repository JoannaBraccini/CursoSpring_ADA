package tech.ada.java.cursospring.api.usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import tech.ada.java.cursospring.api.exception.NaoEncontradoException;

@Service
@RequiredArgsConstructor
@Log4j2
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<UsuarioDTO> listarTodos(@NonNull Pageable pageable) {
        Page<Usuario> usuarios = this.repository.findAll(pageable);
        List<UsuarioDTO> dtoList = Objects.requireNonNull(usuarios.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
        return new PageImpl<>(dtoList, pageable, usuarios.getTotalElements());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorUuidDTO(UUID uuid) {
        Usuario usuario = buscarPorUuid(uuid);
        return usuario != null ? convertToDto(usuario) : null;
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorUuid(UUID uuid) {
        // Usando o repositório para buscar o usuário pelo UUID
        return this.repository.findByUuid(uuid)
                .orElseThrow(
                        () -> new NaoEncontradoException("Usuário não encontrado."));
    }

    public UsuarioDTO convertToDto(Usuario usuario) {
        return this.modelMapper.map(usuario, UsuarioDTO.class);
    }

    @Transactional
    public Usuario criarUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUuid(dto.getUuid() != null ? dto.getUuid() : UUID.randomUUID());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setDob(LocalDate.parse(dto.getDob()));
        usuario.setId(null); // Garante que o banco vai gerar o id
        return this.repository.save(usuario);
    }

    @Transactional
    public Usuario atualizarUsuario(UUID uuid, Usuario usuarioNovo) {
        Usuario usuario = this.buscarPorUuid(uuid);
        usuarioNovo.setId(usuario.getId());
        // Atualizando o usuário no banco de dados com upsert
        return this.repository.save(usuarioNovo);
    }

    @Transactional
    public Usuario alterarNome(UUID uuid, Usuario usuarioAlterado) {
        this.repository.updateNome(uuid, usuarioAlterado.getNome());
        return this.buscarPorUuid(uuid);
    }

    @Transactional
    public String deletarUsuario(UUID uuid) {
        this.repository.deleteByUuid(uuid);
        return "Usuário deletado com sucesso.";
    }

}
