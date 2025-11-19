package tech.ada.java.cursospring.api.amizade;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/amizades")
@RequiredArgsConstructor
public class AmizadeRestController {

    private final AmizadeService service;

    @GetMapping
    public Page<AmizadeDTO> listarTodos(@NonNull Pageable pageable) {
        return this.service.listarTodos(pageable);
    }

    @PostMapping("/{usuarioA}/{usuarioB}")
    public AmizadeDTO criarAmizade(@PathVariable UUID usuarioA, @PathVariable UUID usuarioB) {
        return this.service.criarAmizade(usuarioA, usuarioB);
    }

}
