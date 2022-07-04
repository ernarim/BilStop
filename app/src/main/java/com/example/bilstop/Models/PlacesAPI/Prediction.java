package com.example.bilstop.Models.PlacesAPI;

public class Prediction {
    private String description;

    private String place_id;

    private StructuredFormatting structured_formatting;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public StructuredFormatting getStructured_formatting() {
        return structured_formatting;
    }

    public void setStructured_formatting(StructuredFormatting structured_formatting) {
        this.structured_formatting = structured_formatting;
    }

    @Override
    public String toString() {
        return "Prediction{" +
                "description='" + description + '\'' +
                ", place_id='" + place_id + '\'' +
                ", structured_formatting=" + structured_formatting +
                '}';
    }
}
