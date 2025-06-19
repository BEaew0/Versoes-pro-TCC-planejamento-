package com.example.tesouro_azul_app.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.auth0.android.jwt.JWT;
import com.example.tesouro_azul_app.Class.SuperClassUser;
import com.example.tesouro_azul_app.Service.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

//Essa classe salva as informações do login, e retira elas
public class AuthUtils {
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_JWT_TOKEN = "jwt_token";

    // Salva o token JWT no SharedPreferences
    public static void saveToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_JWT_TOKEN, token).apply();
    }

    // Recupera o token armazenado
    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_JWT_TOKEN, null);
    }

    // Verifica se o usuário está logado (token existe)
    public static boolean isLoggedIn(Context context) {
        return getToken(context) != null;
    }

    // Remove o token (logout)
    public static void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_JWT_TOKEN).apply();
    }

    // Decodifica o payload do JWT para obter dados do usuário (ex: id, email)
    public static JSONObject decodeJwtPayload(String jwtToken) throws JSONException {
        if (jwtToken == null || jwtToken.isEmpty()) return null;

        String[] parts = jwtToken.split("\\."); // Divide o JWT em partes
        if (parts.length < 2) return null;

        // Decodifica a parte do payload (Base64 URL Safe)
        String payloadJson = new String(Base64.decode(parts[1], Base64.URL_SAFE));
        return new JSONObject(payloadJson);
    }

    public static SuperClassUser.TokenInfo getUserInfoFromToken(Context context) {
        String token = getToken(context);
        if (token == null) return null;

        try {
            JSONObject payload = decodeJwtPayload(token);
            if (payload != null) {
                int userId = payload.getInt("nameid");  // Nome do campo pode variar conforme seu token
                String email = payload.getString("email");
                String role = payload.optString("role", "user");  // Se tiver roles

                return new SuperClassUser.TokenInfo(userId, email, role);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
