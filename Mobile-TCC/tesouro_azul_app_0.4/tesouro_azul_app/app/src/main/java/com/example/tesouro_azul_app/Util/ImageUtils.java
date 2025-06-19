package com.example.tesouro_azul_app.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    // Converte Bitmap para String Base64
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream); // 80% qualidade para reduzir tamanho
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Converte Base64 para Bitmap
    public static Bitmap base64ToBitmap(String base64Str) {
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
}