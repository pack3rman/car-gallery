package pl.kurs.java.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;
import pl.kurs.java.model.entity.OfferFoto;

@Data
public class OfferDto {
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
	private int offerTime;
	private String discryption;
	private String mainPhoto;//Base64Encoding image
	private List<String> photos;
}
