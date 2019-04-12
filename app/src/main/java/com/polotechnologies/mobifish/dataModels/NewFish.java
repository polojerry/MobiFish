package com.polotechnologies.mobifish.dataModels;

public class NewFish {
    String fishName;
    String fishPrice;
    String fishDescription;
    String fishImageURl;

    public NewFish() {
    }

    public NewFish(String fishName, String fishPrice, String fishDescriprion, String fishImageURl) {
        this.fishName = fishName;
        this.fishPrice = fishPrice;
        this.fishDescription = fishDescriprion;
        this.fishImageURl = fishImageURl;
    }

    public String getFishName() {
        return fishName;
    }

    public String getFishPrice() {
        return fishPrice;
    }

    public String getFishDescriprion() {
        return fishDescription;
    }

    public String getFishImageURl() {
        return fishImageURl;
    }
}
