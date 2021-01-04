package ru.educationalwork.weatherapplicationjava;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

    private static final String KEY_MAIN = "main";
    private static final String KEY_TEMP = "temp";
    private static final String KEY_WEATHER = "weather";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_NAME = "name";


    public static WeatherItem getWeatherFromJSON(JSONObject jsonObject) {
        try {
            String city = jsonObject.getString(KEY_NAME);
            String temp = jsonObject.getJSONObject(KEY_MAIN).getString(KEY_TEMP);
            String description = jsonObject.getJSONArray(KEY_WEATHER).getJSONObject(0).getString(KEY_DESCRIPTION);
            return new WeatherItem(city, temp, description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}