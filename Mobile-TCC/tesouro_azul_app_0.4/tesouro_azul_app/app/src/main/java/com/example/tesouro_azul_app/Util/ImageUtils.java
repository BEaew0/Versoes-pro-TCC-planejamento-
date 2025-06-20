package com.example.tesouro_azul_app.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    // Converte Bitmap para String Base64
    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) return null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    // Converte Base64 para Bitmap
    public static Bitmap base64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    // Redimensiona um Bitmap para evitar OutOfMemory
    public static Bitmap resizeBitmap(Bitmap original, int maxSize) {
        int width = original.getWidth();
        int height = original.getHeight();

        float ratio = (float) width / height;
        if (width > height) {
            width = maxSize;
            height = (int) (maxSize / ratio);
        } else {
            height = maxSize;
            width = (int) (maxSize * ratio);
        }

        return Bitmap.createScaledBitmap(original, width, height, true);
    }

    public static Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);  // Ajustar qualidade
        byte[] compressedData = out.toByteArray();
        return BitmapFactory.decodeByteArray(compressedData, 0, compressedData.length);
    }

}