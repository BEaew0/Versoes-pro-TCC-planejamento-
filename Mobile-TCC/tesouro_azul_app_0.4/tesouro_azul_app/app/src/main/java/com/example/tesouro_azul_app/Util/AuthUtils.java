package com.example.tesouro_azul_app.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.tesouro_azul_app.Class.SuperClassUser;
import com.example.tesouro_azul_app.Service.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static JSONObject decodeJwtPayload(String jwtToken) throws JSONException {
        if (jwtToken == null || jwtToken.isEmpty()) {
            return null;
        }

        // Divide o token JWT em suas partes
        String[] parts = jwtToken.split("\\.");
        if (parts.length < 2) {
            return null;
        }

        // Decodifica a parte do payload (parte 1)
        String payloadJson = new String(Base64.decode(parts[1], Base64.URL_SAFE));
        return new JSONObject(payloadJson);//Transforma a string decodificada em um objeto JSON.
    }

    public static SuperClassUser.UsuarioTokenDto getUsuarioLogado(Context context) {
        String token = getToken(context);
        if (token == null) return null;

        try {
            JSONObject payload = decodeJwtPayload(token);
            if (payload == null) return null;

            SuperClassUser.UsuarioTokenDto usuario = new SuperClassUser.UsuarioTokenDto();
            usuario.id = payload.optInt("id", -1);  // ou "user_id" se for esse o nome no JWT
            usuario.nome = payload.optString("nome", null);
            usuario.email = payload.optString("email", null);

            return usuario;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getClaimFromToken(Context context, String claimName) {
        String token = AuthUtils.getToken(context);//Usa AuthUtils.getToken() para recuperar o token armazenado.
        if (token == null) return null;

        try {
            JSONObject payload = decodeJwtPayload(token);//Usa AuthUtils.getToken() para recuperar o token armazenado.
            return payload != null ? payload.optString(claimName, null) : null;//Extrai a claim: Usa optString() para obter o valor de forma segura
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
