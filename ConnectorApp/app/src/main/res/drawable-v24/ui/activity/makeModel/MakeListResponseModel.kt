package com.cmrk.ui.activity.makeModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class MakeListResponseModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf()
) : Serializable {
    data class Data(

        @SerializedName("id") var id: String? = null,
        @SerializedName("make") var make: String? = null

    ) : Serializable
}



