package com.jm.jobseekerplatform.service.impl;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service("imageService")
public class ImageService {

    public byte[] resizeLogoEmployer(BufferedImage img) {
        return resizeImage(img, 200, 200);
    }

    public byte[] resizePhotoSeeker(BufferedImage img) {
        return resizeImage(img, 300, 300);
    }

    public byte[] resizeImage(BufferedImage img, int height, int width) {
        int type = img.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : img.getType();
        int finalHeight;
        int finalWidth;

        if (img.getHeight() > height || img.getWidth() > width) {
            finalHeight = height;
            int wid = width;
            float sum = (float)img.getWidth() / (float)img.getHeight();
            finalWidth = Math.round(finalHeight * sum);
            if (finalWidth > wid) {
                finalHeight = Math.round(wid/sum);
                finalWidth = wid;
            }
        } else {
            finalHeight = height;
            finalWidth = width;
        }

        Image tmp = img.getScaledInstance(finalWidth, finalHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(finalWidth, finalHeight, type);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(resized, "png", out);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
