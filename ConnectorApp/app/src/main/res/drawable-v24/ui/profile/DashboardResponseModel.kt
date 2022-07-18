package com.cmrk.ui.profile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Data(
    @SerializedName("employee_key_id") var employeeKeyId: String? = null,
    @SerializedName("employee_id") var employeeId: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("trip_key_id") var tripKeyId: String? = null,
    @SerializedName("trip_id") var tripId: String? = null,
    @SerializedName("visiting_count") var visitingCount: String? = null,
    @SerializedName("lead_count") var leadCount: String? = null,
    @SerializedName("today") var today: Today? = Today(),
    @SerializedName("yesterday") var yesterday: Yesterday? = Yesterday(),
    @SerializedName("month") var month: Month? = Month()
):Serializable

data class DashboardResponseModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()
) : Serializable {

}
data class Month(
    @SerializedName("lead") var lead: Int? = null,
    @SerializedName("followup") var followups: Int? = null,
    @SerializedName("visit") var visit: Int? = null,
    @SerializedName("enquiry") var enquiry:Int?=null
):Serializable

data class Today(
    @SerializedName("lead") var lead: Int? = null,
    @SerializedName("followup") var followups: Int? = null,
    @SerializedName("visit") var visit: Int? = null,
    @SerializedName("enquiry") var enquiry:Int?=null
):Serializable

data class Yesterday(
    @SerializedName("lead") var lead: Int? = null,
    @SerializedName("followup") var followups: Int? = null,
    @SerializedName("visit") var visit: Int? = null,
    @SerializedName("enquiry") var enquiry:Int?=null

):Serializable


