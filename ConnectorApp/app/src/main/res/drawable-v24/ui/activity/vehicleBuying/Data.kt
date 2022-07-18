package com.cmrk.ui.activity.vehicleBuying

data class Data(
    var business_card: ArrayList<String> = arrayListOf<String>(),
    val contact_person: String,
    val date: String,
    val employee_key_id: String,
    val expected_price_per_kg: String,
    val garage_name: String,
    val latitude: String,
    val longitude: String,
    val master_vehicle_id: String,
    val mobile_number: String,
    val monthly_vehicles_vol: String,
    val next_folowup_date: String,
    val no_of_visit: String,
    val owner_mobile_number: String,
    val owner_name: String,
    val photo_of_place: ArrayList<String> = arrayListOf<String>(),
    val remarks: String,
    val scrap_remark: String,
    val scrap_vehicle: String,
    val scrap_vehicle_now: String,
    val trip_key_id: String,
    val visit_id: String,
    val visit_key_id: Int,
    val visiting_count: Int,
    val visitor: String
)