package pl.kurs.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.kurs.java.model.entity.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {

	@Query("select o from Offer o where o.mark like :query or o.model like :query or o.discryption like :query")
	List<Offer> findAllWithQuery(@Param("query") String query);

}
