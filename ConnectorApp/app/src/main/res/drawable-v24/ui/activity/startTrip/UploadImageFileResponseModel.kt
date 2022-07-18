package com.cmrk.ui.activity.startTrip

import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("file") var file: String? = null,
    @SerializedName("full_path") var fullPath: String? = null

)

data class UploadImageFileResponseModel(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()

)


