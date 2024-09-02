package com.axelor.train.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Train {
    @JsonProperty("trainNumber")
    public String trainNumber;

    @JsonProperty("departureCountry")
    private String departureCountry;

    @JsonProperty("departureStation")
    private String departureStation;

    @JsonProperty("destinationStation")
    private String destinationStation;

    @JsonProperty("arrivedDate")
    private String arrivedDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("wagonsNumber")
    private String wagonsNumber;

    @JsonProperty("wagons")
    private List<Wagon> wagons;



    // Getters and setters
}

 class Wagon {
    @JsonProperty("arrivedDate")
    public String arrivedDate;

    @JsonProperty("wagonNumber")
    private long wagonNumber;

    @JsonProperty("wagonCountry")
    private String wagonCountry;

    @JsonProperty("stationCode")
    private String stationCode;

    @JsonProperty("weight")
    private int weight;

    @JsonProperty("wagonTypeCode")
    private String wagonTypeCode;

    @JsonProperty("cargoInfo")
    private List<CargoInfo> cargoInfo;

    // Getters and setters
}

 class CargoInfo {
    @JsonProperty("recipient")
    private Recipient recipient;

    // Getters and setters
}

 class Recipient {
    @JsonProperty("type")
    private String type;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("taxpayerID")
    private String taxpayerID;

    @JsonProperty("okpo")
    private String okpo;

    @JsonProperty("country")
    private String country;

    @JsonProperty("personalINN")
    private String personalINN;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("patronymic")
    private String patronymic;

    @JsonProperty("products")
    public List<Product> products;

    // Getters and setters
}

class Product {
    @JsonProperty("productCode")
    private String productCode;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("productNetWeight")
    private String productNetWeight;

    @JsonProperty("productGrossWeight")
    private String productGrossWeight;

    @JsonProperty("packingType")
    private String packingType;


}
