package pl.kurs.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kurs.java.model.entity.Token;

public interface TokenRepositroy extends JpaRepository<Token, Long> {

}
