package tech.ada.java.cursospring.json;

// import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
// import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

public class JsonReaderDemo {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        // lerComScanner(); // Método alternativo comentado
        lerComHttpClient();

    }

    private static void lerComHttpClient() {
        // Lendo JSON de uma URL usando HttpClient
        HttpClient client = HttpClient.newBuilder().build();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://dummyjson.com/posts/1"))
                    .build(); // Get não é necessário especificar, é o padrão
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()) // Envia a requisição de forma assíncrona
                    .thenApply(stringHttpResponse -> convertJsonToPost(stringHttpResponse.body())) // Extrai o corpo da
                                                                                                   // resposta e
                                                                                                   // converte para Post
                    .thenAccept(post -> System.out.println(post.getTitle())) // Imprime o título do objeto Post
                    .join(); // Aguarda a conclusão da operação assíncrona

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    // Método alternativo para ler JSON usando Scanner
    // private static void lerComScanner() {
    // // Lendo JSON de uma URL usando Scanner
    // try (Scanner scanner = new
    // Scanner(URI.create("https://dummyjson.com/posts/1").toURL().openStream())) {
    // String json = scanner.nextLine(); // Lê a primeira linha do JSON
    // Post post = convertJsonToPost(json);
    // System.out.println(post);
    // } catch (IOException ex) {
    // System.err.println(ex.getMessage());
    // }
    // }

    @SneakyThrows // Evita a necessidade de try-catch para checked exceptions
    private static Post convertJsonToPost(String json) {
        return mapper.readValue(json, Post.class); // Converte JSON em objeto Post
    }
}

// Getter, Setter, ToString, NoArgsConstructor e AllArgsConstructor são
// anotações do Lombok que geram automaticamente os métodos getters, setters,
// toString, construtor sem argumentos e construtor com todos os argumentos.
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class Post {
    private Long id;
    private String title;
    private String body;
    private Long userId;
    private Long views;
    private String[] tags;
    private Reaction reactions;
}

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class Reaction {
    private Long likes;
    private Long dislikes;
}