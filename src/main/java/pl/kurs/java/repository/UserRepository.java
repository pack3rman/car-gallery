package pl.kurs.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kurs.java.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	boolean existsByEmail(String email);

	User findByEmail(String email);
	
	boolean existsByLogin(String login);

	User findByLogin(String login);

}
