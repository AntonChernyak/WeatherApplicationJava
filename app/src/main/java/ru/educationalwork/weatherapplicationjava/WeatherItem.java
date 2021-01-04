package ru.educationalwork.weatherapplicationjava;

public class WeatherItem {
    private String name;
    private String temp;
    private String description;

    public WeatherItem(String name, String temp, String description) {
        this.name = name;
        this.temp = temp;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%s\nТемпература: %s\nНа улице: %s", getName(), getTemp(), getDescription());
    }
}
