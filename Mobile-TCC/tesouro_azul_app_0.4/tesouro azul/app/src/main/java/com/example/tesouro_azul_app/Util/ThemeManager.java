package com.example.tesouro_azul_app.Util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    private static final String PREF_NAME = "ThemePrefs";
    private static final String NIGHT_MODE_KEY = "night_mode";

    // Aplica o tema salvo ao app
    public static void applySavedTheme(Context context) {
        boolean isNightMode = getNightModePreference(context);
        AppCompatDelegate.setDefaultNightMode(
                isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    // Salva a preferência do modo noturno
    public static void saveNightModePreference(Context context, boolean isNightMode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NIGHT_MODE_KEY, isNightMode);
        editor.apply();
    }

    // Recupera a preferência salva
    public static boolean getNightModePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(NIGHT_MODE_KEY, false);
    }
}
