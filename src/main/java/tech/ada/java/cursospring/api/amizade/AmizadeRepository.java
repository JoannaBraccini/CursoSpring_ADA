package tech.ada.java.cursospring.api.amizade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmizadeRepository extends JpaRepository<Amizade, Long> {

}
