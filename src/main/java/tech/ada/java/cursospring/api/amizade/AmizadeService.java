package tech.ada.java.cursospring.api.amizade;

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
import tech.ada.java.cursospring.api.exception.AmizadeInvalidaBusinessException;
import tech.ada.java.cursospring.api.usuario.Usuario;
import tech.ada.java.cursospring.api.usuario.UsuarioService;

@Service
@RequiredArgsConstructor
@Log4j2
public class AmizadeService {

    private final AmizadeRepository repository;
    private final UsuarioService usuarioService;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<AmizadeDTO> listarTodos(@NonNull Pageable pageable) {
        Page<Amizade> usuarios = this.repository.findAll(pageable);
        List<Amizade> content = Optional.ofNullable(usuarios.getContent()).orElseGet(Collections::emptyList);
        List<AmizadeDTO> amizadeDTOList = content.stream().map(this::convertToDto).collect(Collectors.toList());
        return new PageImpl<>(Objects.requireNonNull(amizadeDTOList), pageable, usuarios.getTotalElements());
    }

    public AmizadeDTO convertToDto(Amizade amizade) {
        return this.modelMapper.map(amizade, AmizadeDTO.class);
    }

    @Transactional
    public AmizadeDTO criarAmizade(UUID usuarioA, UUID usuarioB) {
        if (usuarioA.equals(usuarioB)) {
            throw new AmizadeInvalidaBusinessException("É preciso dois usuários diferentes");
        }
        Usuario usuarioEntityA = this.usuarioService.buscarPorUuid(usuarioA);
        Usuario usuarioEntityB = this.usuarioService.buscarPorUuid(usuarioB);

        // Evita duplicidade (A,B) e (B,A)
        boolean existeAB = this.repository.existsByUsuarioAAndUsuarioB(usuarioEntityA, usuarioEntityB);
        boolean existeBA = this.repository.existsByUsuarioAAndUsuarioB(usuarioEntityB, usuarioEntityA);
        if (existeAB || existeBA) {
            throw new AmizadeInvalidaBusinessException("Amizade já existe entre esses usuários");
        }

        Amizade amizade = this.repository.save(new Amizade(usuarioEntityA, usuarioEntityB));
        return this.convertToDto(amizade);
    }

}
