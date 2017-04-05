package com.mobapphome;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageTextDraw {

    public static void main(String[] args) throws Exception {
        String inputDir = args[0];
        String textToDraw = args[1];
        float sizeOfFont = Float.valueOf(args[2]);
        String colorRGBA = args[3];
        int angle = Integer.valueOf(args[4]);

        String outputDir = inputDir + "/decorated";


        File inputFolder = new File(inputDir);
        System.out.println("Input folder = " + inputDir);

        File outputFolder = new File(outputDir);
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }

        for (File fileEntry : inputFolder.listFiles()) {
            if (fileEntry.isDirectory()) {
                continue;
            }

            final BufferedImage orginalImage = ImageIO.read(fileEntry);
            if (orginalImage == null) {
                continue;
            }

            System.out.println(fileEntry.getName());

            int orginalHeight = orginalImage.getHeight();
            int orginalWidth = orginalImage.getWidth();


            int width = orginalWidth;
            int height = orginalHeight;
            BufferedImage image = orginalImage;

//            if (orginalWidth > 400) {
//                width = 400;
//                height = orginalHeight * width / orginalWidth;
//                image = resizeImageWithHint(orginalImage, orginalImage.getType(), width, height);
//                //image = resizeImage(orginalImage, orginalImage.getType(), width, height);
//            }

            //Increse size for good drawing
            int increaseIndex = 6;
            //if(orginalHeight < 500) {
                width = orginalWidth *increaseIndex;
                height = orginalHeight * increaseIndex;
                image = resizeImageWithHint(orginalImage, orginalImage.getType(), width, height);

            //}

            Graphics2D g2 = image.createGraphics();

            g2.setFont(g2.getFont().deriveFont(sizeOfFont* increaseIndex));
            g2.setColor(hex2RgbColor(colorRGBA));

            int widthOfText = g2.getFontMetrics().stringWidth(textToDraw);

            int h = (int) height / 3;
            // for (int w = 0; w < width; w += 30) {
            for (int w = 0; w < width; w += widthOfText) {
                AffineTransform orig = g2.getTransform();
                g2.rotate(angle * -Math.PI / 180, w, h);
                g2.drawString(textToDraw, w, h);
                if (h < height) {
                    h += 50;
                } else {
                    h = 0;
                }
                g2.setTransform(orig);
            }

            //Decrease to orogina size
            image = resizeImageWithHint(image, image.getType(), orginalWidth, orginalHeight);


            g2.dispose();

            ImageIO.write(image, "jpg", new File(outputDir + "/"
                    + fileEntry.getName()));


        }

    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight,
                type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return resizedImage;
    }

    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type, int newWidth, int newHeight) {

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight,
                type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }

    public static Color hex2RgbColor(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16),
                Integer.valueOf(colorStr.substring(7, 9), 16));
    }
}
