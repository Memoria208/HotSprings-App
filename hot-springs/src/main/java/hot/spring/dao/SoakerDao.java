package hot.spring.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hot.spring.entity.Soaker;

public interface SoakerDao extends JpaRepository<Soaker, Long> {

	Optional<Soaker> findBySoakerEmail(String soakerEmail);

}
