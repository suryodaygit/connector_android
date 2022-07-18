package com.cmrk.ui.activity.vehicleDetail

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class VehicleDetailResponseModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()
) : Serializable {
    data class Data(
        @SerializedName("id") var id: String? = null,
        @SerializedName("model") var model: String? = null,
        @SerializedName("make") var make: String? = null,
        @SerializedName("vehicle_type") var vehicleType: String? = null,
        @SerializedName("fuel_type") var fuelType: String? = null,
        @SerializedName("kerb") var kerb: String? = null,
        @SerializedName("scrap_value") var scrapValue: String? = null,
        @SerializedName("spare_value") var spareValue: String? = null,
        @SerializedName("vehicle_value") var vehicleValue: String? = null
    ) : Serializable
}



