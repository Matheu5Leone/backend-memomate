package memo.backend.util;

import org.springframework.web.multipart.MultipartFile;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class ProfilePictureGenerator {

    public static BufferedImage generateProfilePicture(String initials) {
        int size = 200; //px
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        Random random = new Random();
        Color backgroundColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, size, size);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, size / 2));

        FontMetrics fm = g2d.getFontMetrics();
        int x = (size - fm.stringWidth(initials)) / 2;
        int y = (fm.getAscent() + (size - (fm.getAscent() + fm.getDescent())) / 2);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawString(initials, x, y);

        g2d.dispose();
        return image;
    }

    public static void saveImage(BufferedImage image, String filename) {
        File file = new File(filename);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            System.out.println("Error while trying to save image: " + e.getMessage());
        }
    }

    public static String generateLetters(String name) {
        String[] parts = name.split(" ");
        if (parts.length == 1) {
            if (name.length() >= 2) {
                return name.substring(0, 2).toUpperCase();
            } else {
                return name.toUpperCase();
            }
        } else {
            String firstInitial = parts[0].substring(0, 1);
            String secondInitial = parts[1].substring(0, 1);
            return (firstInitial + secondInitial).toUpperCase();
        }
    }

    public static MultipartFile convertBufferedImageToMultipartFile(BufferedImage image, String filename) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        return new MultipartFileMock(inputStream, filename);
    }

    public static byte[] convertBufferedImageToByteArray(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            System.out.println("Error converting image to byte array: " + e.getMessage());
            return null;
        }
    }
}
