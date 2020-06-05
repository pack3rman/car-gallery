package pl.kurs.java.model.form;

import java.util.Date;

import lombok.Data;

@Data
public class OfferForm {
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
	

}
