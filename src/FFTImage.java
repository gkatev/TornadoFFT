import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

// import static math.Cordic.cos;
// import static math.Cordic.sin;
// import static math.Cordic.cordic;

// import static math.RivensMath.cos;
// import static math.RivensMath.sin;

// import static math.TaylorMath.cos;
// import static math.TaylorMath.sin;

import fft.Complex;
import fft.Complex2D;

public class FFTImage {
	public static void main(String args[]) throws Exception {
		if(args.length != 1) {
			System.out.println("Usage: FFTImage <bmp>");
			return;
		}
		
		// double [] tests = { 0.0, 1.1, Math.PI/2.0, Math.PI/3.0, Math.PI*2.0/3.0, };
		// for (int i=0; i<tests.length; i++)
		// {
			// double theta = tests[i];
			// double answer = cos(theta);
			// System.out.printf("Cordic %f is %f cf %f\n", theta, answer, Math.cos(theta));
		// }
		
		// for (int i=0; i<tests.length; i++)
		// {
			// double theta = tests[i];
			// double answer = sin(theta);
			// System.out.printf("Cordic %f is %f cf %f\n", theta, answer, Math.sin(theta));
		// }
		
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
		
		System.out.println("Parsing Image");
		
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
	
	private static void printRgb(int rgb) {
		int r = (rgb & 0xff0000) >> 16;
		int g = (rgb & 0xff00) >> 8;
		int b = rgb & 0xff;
		
		System.out.println(String.format("r=0x%02X, g=0x%02X, b=0x%02X", r, g, b));
	}
}
