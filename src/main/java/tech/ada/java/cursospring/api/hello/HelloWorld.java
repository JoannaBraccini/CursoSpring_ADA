package tech.ada.java.cursospring.api.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HelloWorld {
    @GetMapping("/")
    public Map<String, Object> welcome() {
        return Map.of(
                "mensagem", "Boas vindas ao Projeto CursoSpring!",
                "linkedin", "https://www.linkedin.com/in/joannabraccini",
                "github", "https://github.com/joannabraccini",
                "documentacao", "http://localhost:8080/docs");
    }
}
