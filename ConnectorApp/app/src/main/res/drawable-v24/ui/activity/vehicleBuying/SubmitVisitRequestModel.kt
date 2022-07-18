package com.cmrk.ui.activity.vehicleBuying

import java.io.Serializable

data class SubmitVisitRequestModel(
    var business_card: List<String>?= arrayListOf(),
    var contact_person: String?="",
    var date: String?="",
    var employee_key_id: String?="",
    var expected_price_per_kg: String?="",
    var garage_name: String?="",
    var latitude: String?="",
    var longitude: String?="",
    var master_vehicle_id: String?="",
    var mobile_number: String?="",
    var monthly_vehicles_vol:String?="",
    var next_folowup_date: String?="",
    var no_of_visit: String?="",
    var owner_mobile_number:String?="",
    var owner_name: String?="",
    var photo_of_place: List<String>?= arrayListOf(),
    var remarks: String?="",
    var scrap_remark: String?="",
    var scrap_vehicle: String?="",
    var scrap_vehicle_now: String?="",
    var trip_key_id:String?="",
    var visitor: String?=""
): Serializable