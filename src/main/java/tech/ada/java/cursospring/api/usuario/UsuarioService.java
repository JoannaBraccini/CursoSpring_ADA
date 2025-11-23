package tech.ada.java.cursospring.api.usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Collections;
import java.util.Optional;
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
        List<Usuario> content = Optional.ofNullable(usuarios.getContent()).orElseGet(Collections::emptyList);
        List<UsuarioDTO> dtoList = content.stream().map(this::convertToDto).collect(Collectors.toList());
        return new PageImpl<>(Objects.requireNonNull(dtoList), pageable, usuarios.getTotalElements());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorUuidDTO(UUID uuid) {
        Usuario usuario = buscarPorUuid(uuid);
        return usuario != null ? convertToDto(usuario) : null;
    }

    /**
     * Expondo um método público para outras services resolverem a entidade por
     * UUID.
     * Mantém compatibilidade com chamadas internas que precisem da entidade.
     */
    @Transactional(readOnly = true)
    public Usuario buscarEntidadePorUuid(UUID uuid) {
        return this.buscarPorUuid(uuid);
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
    public UsuarioDTO criarUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUuid(dto.getUuid() != null ? dto.getUuid() : UUID.randomUUID());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setDob(LocalDate.parse(dto.getDob()));
        usuario.setId(null); // Garante que o banco vai gerar o id
        Usuario salvo = this.repository.save(usuario);
        return convertToDto(salvo);
    }

    @Transactional
    public UsuarioDTO atualizarUsuario(UUID uuid, UsuarioDTO usuarioDto) {
        Usuario usuario = this.buscarPorUuid(uuid);
        // Atualiza apenas os campos que fazem sentido a partir do DTO
        usuario.setNome(usuarioDto.getNome() != null ? usuarioDto.getNome() : usuario.getNome());
        usuario.setEmail(usuarioDto.getEmail() != null ? usuarioDto.getEmail() : usuario.getEmail());
        if (usuarioDto.getDob() != null) {
            usuario.setDob(LocalDate.parse(usuarioDto.getDob()));
        }
        Usuario salvo = this.repository.save(usuario);
        return convertToDto(salvo);
    }

    @Transactional
    public UsuarioDTO alterarNome(UUID uuid, UsuarioDTO usuarioDto) {
        if (usuarioDto.getNome() == null) {
            throw new IllegalArgumentException("Nome é obrigatório para alterar-nome");
        }
        this.repository.updateNome(uuid, usuarioDto.getNome());
        Usuario atualizado = this.buscarPorUuid(uuid);
        return convertToDto(atualizado);
    }

    @Transactional
    public String deletarUsuario(UUID uuid) {
        this.repository.deleteByUuid(uuid);
        return "Usuário deletado com sucesso.";
    }

}
