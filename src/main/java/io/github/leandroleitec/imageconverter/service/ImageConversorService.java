package io.github.leandroleitec.imageconverter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;

@Service
public class ImageConversorService {

    private final String savePath;

    public ImageConversorService(@Value("${image.save-path}") String savePath) {
        this.savePath = savePath;
    }

    public String convert(String content, String fileName) throws IOException {
        ByteArrayOutputStream convertedImage = new ByteArrayOutputStream();

        byte[] decodedImage = decode(content);

        ByteArrayInputStream byteArrayInputStream = getByteArray(decodedImage);

        BufferedImage originalImage = ImageIO.read(byteArrayInputStream);

        byteArrayInputStream = getByteArray(decodedImage);

        ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
        boolean isCMYK = verifyCMYKImage(imageInputStream);

        if(isCMYK) {
            BufferedImage rgbImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            ColorConvertOp cco = new ColorConvertOp(null);
            cco.filter(originalImage, rgbImage);
            ImageIO.write(rgbImage, "png", convertedImage);

            // salva um arquivo da imagem
            ImageIO.write(rgbImage, "png", new File(savePath + "/" + fileName + ".png"));
        } else {
            ImageIO.write(originalImage, "png", convertedImage);

            // salva um arquivo da imagem
            ImageIO.write(originalImage, "png", new File(savePath + "/" + fileName + ".png"));
        }

        return enconde(convertedImage);
    }

    private String enconde(ByteArrayOutputStream image) {
        return Base64.getEncoder().encodeToString(image.toByteArray());
    }

    private ByteArrayInputStream getByteArray(byte[] image) {
        return new ByteArrayInputStream(image);
    }

    private byte[] decode(String base64Image) {
        return Base64.getDecoder().decode(base64Image);
    }

    private boolean verifyCMYKImage(ImageInputStream imageInputStream) throws IOException {
        ImageReader reader = ImageIO.getImageReaders(imageInputStream).next();
        reader.setInput(imageInputStream);

        Iterator<ImageTypeSpecifier> imageTypes = reader.getImageTypes(0);
        for (Iterator<ImageTypeSpecifier> it = imageTypes; it.hasNext(); ) {
            ImageTypeSpecifier type = it.next();
            if (type.getColorModel().getColorSpace().getType() == ColorSpace.TYPE_CMYK) {
                return true;
            }
        }
        return false;
    }
}
