package pl.kurs.java.coverter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import pl.kurs.java.dto.OfferDto;
import pl.kurs.java.model.entity.Offer;
import pl.kurs.java.model.entity.OfferFoto;

public class OfferToOfferDtoConverter implements Converter<Offer, OfferDto> {

	@Override
	public OfferDto convert(MappingContext<Offer, OfferDto> context) {

		Offer source = context.getSource();

		OfferDto dto = new OfferDto();
		dto.setAcceleration0100(source.getAcceleration0100());
		dto.setId(source.getId());
		dto.setMark(source.getMark());
		dto.setModel(source.getModel());
		dto.setProductionDate(source.getProductionDate());
		dto.setRegistrationCountry(source.getRegistrationCountry());
		dto.setRegistrationDate(source.getRegistrationDate());
		dto.setAccident(source.isAccident());
		dto.setMileage(source.getMileage());
		dto.setInvoice(source.isInvoice());
		dto.setHp(source.getHp());
		dto.setWeight(source.getWeight());
		dto.setAcceleration100200(source.getAcceleration100200());
		dto.setFuelType(source.getFuelType());
		dto.setPrice(source.getPrice());
		dto.setOfferTime(source.getOfferTime());
		dto.setDiscryption(source.getDiscryption());
		dto.setMainPhoto(findAndConvertMainPhoto(source));
		dto.setPhotos(findAndConvertAllRealPhotos(source.getPhotos()));
		return dto;
	}

	private String findAndConvertMainPhoto(Offer source) {
		try {
			byte[] fileContent = FileUtils
					.readFileToByteArray(new File(source.getPhotos().get(0).getPath() + "_SMALL"));
			return Base64.getEncoder().encodeToString(fileContent);
		} catch (Exception e) {
			return findAndConvertMainRealPhoto(source);
		}

	}

	private String findAndConvertMainRealPhoto(Offer source) {
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(new File(source.getPhotos().get(0).getPath()));
			return Base64.getEncoder().encodeToString(fileContent);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private List<String> findAndConvertAllRealPhotos(List<OfferFoto> source) {
		try {
			List<String> filsContent = new ArrayList<>();
			for (OfferFoto o : source) {
				byte[] fileContent = FileUtils.readFileToByteArray(new File(o.getPath()));
				filsContent.add(Base64.getEncoder().encodeToString(fileContent));
			}
			return filsContent;
		} catch (Exception e) {
			e.printStackTrace();
			return Arrays.asList();
		}
	}

}
