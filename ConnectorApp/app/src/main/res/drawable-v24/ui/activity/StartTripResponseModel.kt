package com.cmrk.ui.activity

import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("start_latitude") var startLatitude: String? = null,
    @SerializedName("start_longitude") var startLongitude: String? = null,
    @SerializedName("start_km") var startKm: String? = null,
    @SerializedName("start_date_time") var startDateTime: String? = null,
    @SerializedName("trip_status") var tripStatus: Int? = null,
    @SerializedName("trip_id") var tripId: Int? = null

)

data class StartTripResponseModel(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()

)


