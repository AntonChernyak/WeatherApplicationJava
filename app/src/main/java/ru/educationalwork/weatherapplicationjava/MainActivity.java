package ru.educationalwork.weatherapplicationjava;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private EditText editTextCity;
    private TextView textViewWeather;
    private LoaderManager loaderManager;

    private static final int LOADER_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        textViewWeather = findViewById(R.id.textViewWeather);

        loaderManager = LoaderManager.getInstance(this);
    }

    public void onClickShowWeather(View view) {
        String city = editTextCity.getText().toString().trim();

        if (!city.isEmpty()) {
            downloadData(city);
        }
        // скрыть клавиатуру
        editTextCity.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }

    // Загрузка данных
    private void downloadData(String city) {
        URL url = NetworkUtils.buildURL(city); // создаём url
        Bundle bundle = new Bundle();
        bundle.putString("url", url.toString());
        // добавляем загрузчик. restart --- проверит, есть ли уже loader
        loaderManager.restartLoader(LOADER_ID, bundle, this); // callback == this, т.к. все слушатеи loader реализовали в активити
    }


    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        // id тут --- уникальный идентификатор загрузчика. Указываем сами.
        return new NetworkUtils.DownloadJSONTask(this, args); // получили данные
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        if (data != null) {
            WeatherItem weatherItem = JSONUtils.getWeatherFromJSON(data);
            if (weatherItem != null) {
                textViewWeather.setText(weatherItem.toString());
            }
        } else {
            textViewWeather.setText(R.string.error_message);
        }
        // после завершения загрузки удаляем loader
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }

}