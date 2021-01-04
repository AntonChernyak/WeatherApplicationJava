package ru.educationalwork.weatherapplicationjava;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// ссылка на API: https://home.openweathermap.org/
// api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
// http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=13b60f2a4ae44b707ae2039f503d3b7d&lang=ru&units=metric
public class NetworkUtils {

    private static final String BASE_URL_CITY = "http://api.openweathermap.org/data/2.5/weather";
    private static final String PARAMS_API_KEY = "APPID";
    private static final String PARAMS_CITY = "q";
    private static final String PARAMS_LANG = "lang";
    private static final String PARAMS_UNITS = "units";

    private static final String API_KEY = "13b60f2a4ae44b707ae2039f503d3b7d";
    private static final String LANG = "ru";
    private static final String UNITS = "metric";
    //private static final String ID = "46a60b8be456e983ef4cbdace3396a89";

    public   static class DownloadJSONTask extends AsyncTaskLoader<JSONObject>{

        private final Bundle bundle;
        private OnStartLoadingListener onStartLoadingListener;

        // слушатель, реагирующий на начало загрузки
        public interface OnStartLoadingListener {
            void onStartLoading();
        }

        // setter
        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        // Конструктор. Источник данных (тут url) бычно передают через Bundle
        public DownloadJSONTask(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        // Чтобы при инициализации этого загрузчика происходила загрузка переопределим метод onStartLoading()
        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (onStartLoadingListener != null) {
                onStartLoadingListener.onStartLoading();
            }
            forceLoad(); // продолжает загрузку
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            if (bundle == null) {
                return null;
            }
            String urlAsString = bundle.getString("url");
            URL url = null;
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            JSONObject result = null;

            if (url == null) {
                return null;
            }
            // Если с url всё в порядке, то создаём соединение
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                // Чтобы читать сразу строками создадим BufferedReader
                BufferedReader reader = new BufferedReader(inputStreamReader);

                StringBuilder builder = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                try {
                    result = new JSONObject(builder.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // не забыть закрыть соединение!
                if (connection != null) {
                    connection.disconnect();
                }
            }
            if (result != null) {
            }
            return result;
        }
    }

    public static URL buildURL(String city) {
        URL result = null;
        Uri uri = Uri.parse(BASE_URL_CITY).buildUpon()
                .appendQueryParameter(PARAMS_CITY, city)
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANG, LANG)
                .appendQueryParameter(PARAMS_UNITS, UNITS)
                .build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
