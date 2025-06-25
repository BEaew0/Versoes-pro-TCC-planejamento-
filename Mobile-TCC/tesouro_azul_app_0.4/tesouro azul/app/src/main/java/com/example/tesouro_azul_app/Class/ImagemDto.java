package com.example.tesouro_azul_app.Class;

public class ImagemDto {
    private String imagemBase64;

    public ImagemDto(String imagemBase64) {
        this.imagemBase64 = imagemBase64;
    }

    public String getImagemBase64() {
        return imagemBase64;
    }

    public void setImagemBase64(String imagemBase64) {
        this.imagemBase64 = imagemBase64;
    }
}

