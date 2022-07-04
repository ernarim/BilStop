package com.example.bilstop.Models;

import java.util.ArrayList;

public class Root{

    private ArrayList<Result> results;

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public class Result{
        private ArrayList<AddressComponent> address_components;
        private String formatted_address;
        private String place_id;
        private ArrayList<String> types;

        public ArrayList<AddressComponent> getAddress_components() {
            return address_components;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public ArrayList<String> getTypes() {
            return types;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setAddress_components(ArrayList<AddressComponent> address_components) {
            this.address_components = address_components;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public void setTypes(ArrayList<String> types) {
            this.types = types;
        }

        public class AddressComponent{
            private String long_name;
            private String short_name;
            private ArrayList<String> types;

            public String getLong_name() {
                return long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public ArrayList<String> getTypes() {
                return types;
            }

            public void setLong_name(String long_name) {
                this.long_name = long_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public void setTypes(ArrayList<String> types) {
                this.types = types;
            }
        }
    }


}
