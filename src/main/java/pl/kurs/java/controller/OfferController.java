package pl.kurs.java.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pl.kurs.java.dto.OfferDto;
import pl.kurs.java.model.entity.Offer;
import pl.kurs.java.model.entity.OfferFoto;
import pl.kurs.java.model.form.OfferForm;
import pl.kurs.java.repository.OfferRepository;
import pl.kurs.java.repository.TokenRepositroy;
import pl.kurs.java.repository.UserRepository;
import pl.kurs.java.service.EmailService;
import pl.kurs.java.service.ImageProcessorService;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
//@Profile("dev")
public class OfferController {
	private final OfferRepository offerRepository;
	private final UserRepository userReposiotry;
	private final TokenRepositroy tokenRepository;
	private final EmailService emailService;
	private final ImageProcessorService imageProcessorService;
	private final ModelMapper mapper;
	

	@GetMapping("")
	public String enterPage(ModelMap model,
			@RequestParam(name = "query", required = false, defaultValue = "") String query) {
		model.addAttribute("offers", offerRepository.findAllWithQuery("%" + query + "%").stream()
				.map(entity -> mapper.map(entity, OfferDto.class)).collect(Collectors.toList()));
		return "offerView";
	}

	@GetMapping("/details/{id}")
	public String enterDetailsPage(ModelMap model, @PathVariable("id") long id) {
		model.addAttribute("offer", mapper.map(offerRepository.findById(id).get(), OfferDto.class));
		return "offerDetailView";
	}

	@PostMapping("")
	public String offer(ModelMap model, @ModelAttribute OfferForm form, @RequestParam("photos") MultipartFile file)
			throws IOException {
		UUID uuid = UUID.randomUUID();

		File targetFile = new File("c:/zdjecia/" + uuid + file.getOriginalFilename());

		FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);

		imageProcessorService.processImage(targetFile.getAbsolutePath());

		OfferFoto of = OfferFoto.builder()//
				.path(targetFile.getAbsolutePath())//
				.build();

		Offer offer1 = mapper.map(form, Offer.class);
		offer1.setPhotos(Arrays.asList(of));

		offerRepository.saveAndFlush(offer1);

		model.addAttribute("offers", offerRepository.findAll().stream()
				.map(entity -> mapper.map(entity, OfferDto.class)).collect(Collectors.toList()));
		return "offerView";
	}

	@PostMapping("/add")
	public String addMorePhotos(ModelMap model, @RequestParam("files") MultipartFile[] uploadfiles, @RequestParam(value = "id") long id) throws IOException {
		
		Offer offer = offerRepository.findById(id).orElse(null);
		
		
		for (MultipartFile file : uploadfiles) {
			if (file.isEmpty()) {
				continue;
			}
			UUID uuid = UUID.randomUUID();

			File targetFile = new File("c:/zdjecia/" + uuid + file.getOriginalFilename());

			FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);

			imageProcessorService.processImage(targetFile.getAbsolutePath());
			
			OfferFoto of = OfferFoto.builder()//
					.path(targetFile.getAbsolutePath())//
					.build();
			
			System.out.println(file.getOriginalFilename());
			offer.getPhotos().add(of);
		}
		offer = offerRepository.saveAndFlush(offer);
		model.addAttribute("offer", mapper.map(offer, OfferDto.class));
		//offer.getPhotos().add(.file..)
		return "offerDetailView";
	}

	@GetMapping("/delete")
	public String delete(ModelMap model, @RequestParam(value = "id") long id) {
		System.out.println("zaczunam");
		offerRepository.deleteById(id);
		Optional<Offer> usuwany = offerRepository.findById(id);
		System.out.println(usuwany);
		System.out.println("returning to list of cars");
		return "redirect:/offer/";
	}

	@InitBinder
	public void allowEmptyDateBinding(WebDataBinder binder) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
	}

}
