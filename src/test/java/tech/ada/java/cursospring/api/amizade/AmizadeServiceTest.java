package tech.ada.java.cursospring.api.amizade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import tech.ada.java.cursospring.api.exception.AmizadeInvalidaBusinessException;
import tech.ada.java.cursospring.api.usuario.Usuario;
import tech.ada.java.cursospring.api.usuario.UsuarioService;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class AmizadeServiceTest {

    @Mock
    private AmizadeRepository amizadeRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private AmizadeService amizadeService;

    @Test
    void falhaCriarAmizade() {
        // cenário
        UUID usuarioA = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID usuarioB = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        // ação
        // this.amizadeService.criarAmizade(usuarioA, usuarioB);
        // validacao
        Assertions.assertThrows(AmizadeInvalidaBusinessException.class, () -> {
            this.amizadeService.criarAmizade(usuarioA, usuarioB);
        });
    }

    @Test
    @DisplayName("Sucesso")
    void criarAmizade() {
        // cenário
        UUID usuarioA = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID usuarioB = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

        Mockito.when(this.usuarioService.buscarPorUuid(Mockito.<UUID>any())).thenReturn(new Usuario());
        Mockito.when(this.amizadeRepository.save(Mockito.<Amizade>any())).thenReturn(new Amizade());
        Mockito.when(this.modelMapper.map(Mockito.<Amizade>any(), eq(AmizadeDTO.class)))
                .thenReturn(new AmizadeDTO());

        // ação
        AmizadeDTO amizadeDTO = this.amizadeService.criarAmizade(usuarioA, usuarioB);

        // validação
        Assertions.assertEquals(new AmizadeDTO(), amizadeDTO);
        // Obs: Para funcionar precisa implementar equals and hashcode em AmizadeDTO
    }

    @Test
    void testListarTodosReturnsPageOfAmizadeDTO() {
        Usuario usuarioA = new Usuario();
        Usuario usuarioB = new Usuario();
        Amizade amizade = new Amizade(usuarioA, usuarioB);
        List<Amizade> amizades = Arrays.asList(amizade);
        Page<Amizade> page = new PageImpl<>(amizades);
        when(amizadeRepository.findAll(Mockito.<Pageable>any())).thenReturn(page);
        when(modelMapper.map(Mockito.<Amizade>any(), eq(AmizadeDTO.class))).thenReturn(new AmizadeDTO());

        Page<AmizadeDTO> result = amizadeService.listarTodos(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertNotNull(result.getContent().get(0));

        verify(amizadeRepository).findAll(Mockito.<Pageable>any());
        verify(modelMapper, times(1)).map(Mockito.<Amizade>any(), eq(AmizadeDTO.class));
    }

    @Test
    void testConvertToDtoMapsCorrectly() {
        Usuario usuarioA = new Usuario();
        Usuario usuarioB = new Usuario();
        Amizade amizade = new Amizade(usuarioA, usuarioB);
        when(modelMapper.map(Mockito.<Amizade>any(), eq(AmizadeDTO.class))).thenReturn(new AmizadeDTO());

        AmizadeDTO dto = amizadeService.convertToDto(amizade);

        assertNotNull(dto);
        verify(modelMapper).map(any(Amizade.class), eq(AmizadeDTO.class));
    }

    @Test
    void testCriarAmizadeThrowsExceptionForSameUser() {
        UUID uuid = UUID.randomUUID();
        assertThrows(AmizadeInvalidaBusinessException.class, () -> {
            amizadeService.criarAmizade(uuid, uuid);
        });
    }

    @Test
    void testCriarAmizadeCreatesAndReturnsDTO() {
        UUID uuidA = UUID.randomUUID();
        UUID uuidB = UUID.randomUUID();
        Usuario usuarioA = new Usuario();
        Usuario usuarioB = new Usuario();
        Amizade amizade = new Amizade(usuarioA, usuarioB);

        when(usuarioService.buscarPorUuid(uuidA)).thenReturn(usuarioA);
        when(usuarioService.buscarPorUuid(uuidB)).thenReturn(usuarioB);
        when(amizadeRepository.save(isA(Amizade.class))).thenReturn(amizade);
        when(modelMapper.map(Mockito.<Amizade>any(), eq(AmizadeDTO.class))).thenReturn(new AmizadeDTO());

        AmizadeDTO dto = amizadeService.criarAmizade(uuidA, uuidB);

        assertNotNull(dto);
        verify(usuarioService).buscarPorUuid(uuidA);
        verify(usuarioService).buscarPorUuid(uuidB);
        verify(amizadeRepository).save(isA(Amizade.class));
        verify(modelMapper).map(Mockito.<Amizade>any(), eq(AmizadeDTO.class));
    }
}