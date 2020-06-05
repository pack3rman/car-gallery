package pl.kurs.java.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ImageProcessorService {

	//@Async
	public void processImage(String orignalPath) {
		try {
			BufferedImage image = ImageIO.read(new File(orignalPath));

			double round = (double) image.getWidth() / 435;
			double scale = 1.0 / round;
			System.out.println("Skalowanie obrazka: " + orignalPath + " wg skalu: " + scale);
			BufferedImage bi = new BufferedImage((int) (scale * image.getWidth(null)),
					(int) (scale * image.getHeight(null)), BufferedImage.TYPE_INT_RGB);

			Graphics2D grph = (Graphics2D) bi.getGraphics();
			grph.scale(scale, scale);

			grph.drawImage(image, 0, 0, null);
			grph.dispose();

			ImageIO.write(bi, "jpg", new File(orignalPath + "_SMALL"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
