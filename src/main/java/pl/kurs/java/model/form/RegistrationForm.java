package pl.kurs.java.model.form;

import lombok.Data;

@Data
public class RegistrationForm {
	private String login;
	private String name;
	private String surrName;
	private String email;
	private String password;
	private String password2;

}
