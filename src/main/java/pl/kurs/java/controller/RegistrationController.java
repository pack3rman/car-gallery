package pl.kurs.java.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;
import pl.kurs.java.model.entity.Token;
import pl.kurs.java.model.entity.User;
import pl.kurs.java.model.form.RegistrationForm;
import pl.kurs.java.repository.OfferRepository;
import pl.kurs.java.repository.TokenRepositroy;
import pl.kurs.java.repository.UserRepository;
import pl.kurs.java.service.EmailService;

@Controller
@RequestMapping("/registration")
@AllArgsConstructor
public class RegistrationController {
	private final OfferRepository offerRepository;
	private final UserRepository userReposiotry;
	private final TokenRepositroy tokenRepository;
	private final EmailService emailService;
	
	@PostMapping("/")
	public String enterPage(ModelMap model , @ModelAttribute RegistrationForm form){
		boolean error = true;
		List<String> fails = new ArrayList<>();
		System.out.println(form);
		System.out.println(fails);
		if(form.getLogin().isEmpty()){
			fails.add("brak loginu");
		}
		if (form.getSurrName().isEmpty()) {
			System.out.println("brak Nawiska");
			fails.add("brak Nawiska");
		}
		if (form.getName().isEmpty()) {
			System.out.println("brak imienia");
			fails.add("brak imienia");
		}
		if (form.getEmail().isEmpty()) {
			System.out.println("brak emaila");
			fails.add("brak emaila");
		}
		if (form.getPassword().isEmpty()) {
			System.out.println("brak hasla");
			fails.add("brak hasla");
		}
		if (!form.getEmail().contains("@")) {
			System.out.println("niepoprawny email");
			fails.add("niepoprawny email");
		}
		if (form.getPassword().length() <= 5) {
			System.out.println("za krotkie haslo");
			fails.add("za krotkie haslo");
		}
		if (!form.getPassword().equals(form.getPassword2())) {
			System.out.println("hasla nie pasuja");
			fails.add("hasla nie pasuja");
		}
		if(userReposiotry.existsByLogin(form.getLogin())){
			fails.add("login jest zajety");
		}
		boolean exists = userReposiotry.existsByEmail(form.getEmail());
		if (exists) {
			System.out.println("email juz istnieje w bazie");
			fails.add("email juz istnieje w bazie");
		}
		if (fails.isEmpty()) {
			error = false;
			User u1 = User.builder()//
					.login(form.getLogin())//
					.name(form.getName())//
					.surrName(form.getSurrName())//
					.email(form.getEmail())//
					.password(form.getPassword())//
					.build();//

			// generowanie + zapisanie token
			// id (genenruje baza)
			// value (String - UUid)
			// expirationDate
			// used
			// token i user sa w relacji onetoone
			UUID uuid = UUID.randomUUID();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1);
			u1 = userReposiotry.saveAndFlush(u1);
			Token token = Token.builder()//
					.uuid(uuid.toString())//
					.user(u1)//
					.expirationDate(cal.getTime())//
					.build();

			tokenRepository.saveAndFlush(token);
			emailService.sendEmail(u1, token);

		} else {
			model.addAttribute("fails", fails);
		}

		model.addAttribute("hasErrors", error);
		model.addAttribute("offers", offerRepository.findAll());
		return "offerView";
	}
	@GetMapping("/verify")
	public String verifyToken(ModelMap model, @RequestParam("token") String token,
			@RequestParam("email") String email) {
		boolean errorT = true;
		// ..
		// "http://localhost:8080/registration/verify?token="+token.getUuid()+"&email="+email.getEmail()
		model.addAttribute("users", userReposiotry.findAll());
		User user = userReposiotry.findByEmail(email);
		Calendar cal = Calendar.getInstance();
		List<String> tokenFails = new ArrayList<>();
		if (user == null) {
			tokenFails.add("niepoprawny email");
			model.addAttribute("hasErrorsT", errorT);
			model.addAttribute("tokenFails", tokenFails);

			model.addAttribute("users", userReposiotry.findAll());
			return "offerView";
		}

		System.out.println("zaczynam werfikacje");

		if (!user.getEmail().equals(email)) {
			tokenFails.add("niepoprawny email");
		}
		if (!user.getToken().getExpirationDate().after(cal.getTime())) {
			tokenFails.add("token wygasł zarejestruj sie ponownie");
		}
		if (user.getToken().isUsed()) {
			tokenFails.add("token został już wykorzystany");
		}
		if (!user.getToken().getUuid().equals(token)) {
			tokenFails.add("niepoprawny token");
		}
		System.out.println(tokenFails);
		if (tokenFails.isEmpty()) {
			errorT = false;
			System.out.println("jestem tu");
			user.setActive(true);
			user.getToken().setUsed(true);
			tokenFails.add("email Potwierdzony");
			userReposiotry.saveAndFlush(user);
			tokenRepository.saveAndFlush(user.getToken());

		}
		System.out.println("koncze werfikacje");

		model.addAttribute("hasErrorsT", errorT);
		model.addAttribute("tokenFails", tokenFails);

		model.addAttribute("users", userReposiotry.findAll());
		return "offerView";
	}

}
