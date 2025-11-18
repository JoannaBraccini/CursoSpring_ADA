package tech.ada.java.cursospring.api.usuario;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    @Modifying // Indica que o método modifica dados no banco
    @Query("UPDATE Usuario u SET u.nome = :nome WHERE u.uuid = :uuid") // JPQL (linguagem de consulta do JPA)
    void updateNome(@Param("uuid") UUID uuid, @Param("nome") String nome);

}