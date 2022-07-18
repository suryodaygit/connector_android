package com.cmrk.ui.activity.makeModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ModelListResponseModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf()
) : Serializable {
    data class Data(
        @SerializedName("id") var id: String? = null,
        @SerializedName("model") var model: String? = null,
        @SerializedName("fuel_type") var fuelType: String? = null
    ) : Serializable
}



