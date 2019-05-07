package com.polotechnologies.mobifish.dataModels;

public class Cart {
    String fishName;
    String fishImageUrl;
    String fishQuantity;
    String fishpriceEach;
    String fishTotalPrice;
    String cartId;
    String conctactNumber;
    String userId;

    public Cart() {
    }

    public Cart(String fishName, String fishImageUrl, String fishQuantity, String fishpriceEach, String fishTotalPrice, String cartId, String conctactNumber, String userId) {
        this.fishName = fishName;
        this.fishImageUrl = fishImageUrl;
        this.fishQuantity = fishQuantity;
        this.fishpriceEach = fishpriceEach;
        this.fishTotalPrice = fishTotalPrice;
        this.cartId = cartId;
        this.conctactNumber = conctactNumber;
        this.userId = userId;
    }

    public String getFishName() {
        return fishName;
    }

    public String getFishImageUrl() {
        return fishImageUrl;
    }

    public String getFishQuantity() {
        return fishQuantity;
    }

    public String getFishpriceEach() {
        return fishpriceEach;
    }

    public String getFishTotalPrice() {
        return fishTotalPrice;
    }

    public String getCartId() {
        return cartId;
    }

    public String getConctactNumber() {
        return conctactNumber;
    }

    public String getUserId() {
        return userId;
    }
}
