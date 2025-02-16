package com.example.weatherdemo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class model {

    Main main;
    Wind wind;
    Sys sys;
    List<Weather> weather;
    String cityName;

    public model(com.example.weatherdemo.Wind wind, Sys sys, List<Weather> weather) {
        this.wind = wind;
        this.sys = sys;
        this.weather = weather;
    }

    static class weatherCondition{
        String main;
    }

    public String getweatherCondition(){
        return weather.get(0).main;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public String getCityName() {
        return cityName;
    }

}
