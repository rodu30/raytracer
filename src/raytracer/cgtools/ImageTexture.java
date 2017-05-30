package raytracer.cgtools;

import raytracer.raytracer.Vec3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTexture {
	private BufferedImage	image;
	private int				width;
	private int				height;

	public ImageTexture(String filename) throws IOException {
		image = ImageIO.read(new File(filename));
		width = image.getWidth();
		height = image.getHeight();
	}

	public Vec3 samplePoint(double u, double v) {
		int x = (int) Math.floor(clamp(u) * (width - 1));
		int y = (int) Math.floor(clamp(v) * (height - 1));

		Color rgb = new Color(image.getRGB(x, y));
		return new Vec3(rgb.getRed() / 255.0, rgb.getGreen() / 255.0,
				rgb.getBlue() / 255.0);
	}

	private double clamp(double v) {
		return Math.abs(v % 1.0);
	}
}
