package com.polotechnologies.mobifish.dataModels;

public class NewFish {
    String fishId;
    String fishName;
    String fishPrice;
    String fishDescription;
    String fishImageURl;
    String fishermanId;
    String fisherManContactNumber;

    public NewFish() {
    }

    public NewFish(String fishId, String fishName, String fishPrice, String fishDescription, String fishImageURl, String fishermanId, String fisherManContactNumber) {
        this.fishId = fishId;
        this.fishName = fishName;
        this.fishPrice = fishPrice;
        this.fishDescription = fishDescription;
        this.fishImageURl = fishImageURl;
        this.fishermanId = fishermanId;
        this.fisherManContactNumber = fisherManContactNumber;
    }

    public String getFishId() {
        return fishId;
    }

    public String getFishName() {
        return fishName;
    }

    public String getFishPrice() {
        return fishPrice;
    }

    public String getFishDescription() {
        return fishDescription;
    }

    public String getFishImageURl() {
        return fishImageURl;
    }

    public String getFishermanId() {
        return fishermanId;
    }

    public String getFisherManContactNumber() {
        return fisherManContactNumber;
    }
}
