package pl.kurs.java.model.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String mark;
	private String model;
	private int productionDate;
	private Date registrationDate;
	private String registrationCountry;
	private boolean accident;
	private int mileage;
	private boolean invoice;
	private int hp;
	private int weight;
	private double acceleration0100;
	private double acceleration100200;
	private String fuelType;
	private int price;
	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "offer_id")
	private List<OfferFoto> photos;
	private int offerTime;
	private String discryption;

}
