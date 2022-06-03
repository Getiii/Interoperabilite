package fr.ensim.interop.introrest.model.weather;

import java.util.List;

public class OpenWeatherList {

    public List<InfoOpenWeatherList> list;

    public List<InfoOpenWeatherList> getList() {
        return list;
    }

    public void setList(java.util.List<InfoOpenWeatherList> list) {
        this.list = list;
    }

    public OpenWeatherList() {
    }
}
