package org.example.view;

import javafx.scene.image.Image;

import java.io.*;

public class ImageUtils {
    public static byte[] imageToByteArray(String imagePath) throws IOException {
        File file = new File(imagePath);
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }
    public static Image byteArrayToImage(byte[] imageData) { return new Image(new ByteArrayInputStream(imageData));}
}
