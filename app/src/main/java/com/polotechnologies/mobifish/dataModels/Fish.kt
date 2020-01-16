package com.polotechnologies.mobifish.dataModels

data class Fish(
        var fishId : String = "",
        var fishType : String = "",
        var fishDescription : String = "",
        var fishPrice : String = "",
        var fishQuantity: String  = "",
        var fishImageUrl : String = "",
        var fisherManId: String = ""
)