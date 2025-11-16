package tech.ada.java.cursospring.api;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Postagem {

    private UUID uuid;
    private String titulo;
    private String corpo;
    private LocalDateTime dataHora;
    private tech.ada.java.cursospring.api.Usuario autor;
}
