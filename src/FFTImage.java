import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import fft.Complex;
import fft.Complex2D;

public class FFTImage {
	public static void main(String args[]) throws Exception {
		if(args.length != 1) {
			System.out.println("Usage: FFTImage <bmp>");
			return;
		}
		
		File imageFile = new File(args[0]);
		BufferedImage image = ImageIO.read(imageFile);
		
		apply1DFFT(image);
		
		File resultFile = new File("fft-result.bmp");
		ImageIO.write(image, "bmp", resultFile);
	}
	
	private static void apply1DFFT(BufferedImage image) throws Exception {
		long start, end;
		
		int pixels_x = image.getWidth();
		int pixels_y = image.getWidth();
		
		System.out.println("Allocating memory");
		double[] data = new double[pixels_x * pixels_y * 2];
		
		System.out.printf("Parsing Image (%d pixels)\n", pixels_x * pixels_y);
		
		int data_idx = 0;
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				int rgb = image.getRGB(x, y);
				
				data[data_idx] = rgb & 0xff;
				data_idx += 2;
			}
		}
		
		System.out.println("Applying FFT");
		
		start = System.nanoTime();
		new Complex(pixels_x * pixels_y).fft(data, 0, 2);
		end = System.nanoTime();
		
		System.out.println("Time: " + (end - start) / 1e06 + " ms.");
		
		System.out.println("Writing result");
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				int idx = 2 * y * image.getWidth() + 2 * x;
				
				int g = (int) data[idx];
				int rgb = 0xFF000000 + (g << 16) + (g << 8) + g;
				
				image.setRGB(x, y, rgb);
			}
		}
	}
}
