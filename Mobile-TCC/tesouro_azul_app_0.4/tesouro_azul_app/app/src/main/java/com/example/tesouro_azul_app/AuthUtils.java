package com.example.tesouro_azul_app;

import android.content.Context;
import android.content.SharedPreferences;

//Essa classe salva as informações do login, e retira elas
public class AuthUtils {

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getString("jwt_token", null) != null;
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getString("jwt_token", null);
    }

    public static void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        prefs.edit().remove("jwt_token").apply();

        // Reseta cliente Retrofit
        RetrofitClient.resetClient();
    }
}
