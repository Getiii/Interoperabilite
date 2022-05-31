package fr.ensim.interop.introrest.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Weather {

    private Integer id;
    private String main;
    private String description;

    public Weather() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
