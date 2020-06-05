package pl.kurs.java;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import pl.kurs.java.coverter.OfferToOfferDtoConverter;

@SpringBootApplication
@EnableAsync
public class ApplicationCarDealler {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationCarDealler.class, args);
	}
	
	@Bean
	public ModelMapper mapper(){
		ModelMapper mapper = new ModelMapper();
		mapper.addConverter(new OfferToOfferDtoConverter());
		return mapper;
	}
	
	@Bean
	public int counterForOffers(){
		return 0;
	}
	
	@Bean
	public int counterForCars(){
		return 0;
	}
	
	
}
