package com.cmrk.ui.activity.sparepartsSelling

data class Data(
    val daily_offer_interest: String,
    val employee_key_id: String,
    val expected_amount: String,
    val fuel_type: String,
    val immediate_requirement: String,
    val kerb_weight: String,
    val lead_id: String,
    val lead_key_id: Int,
    val make: String,
    val manufacture_year: String,
    val master_vehicle_id: String,
    val model: String,
    val orc_available: String,
    val price_expectation_per_kg: String,
    val registration_number: String,
    val scrap_vehicle_id: String,
    val scrap_vehicle_key_id: Int,
    val spare_part_requirement: String,
    val specific_spare_part: String,
    val tank_capacity: String,
    val tank_expiry_date: String,
    val towing_charge: String,
    val trip_key_id: String,
    val vehicle_condition: String,
    val vehicle_images: ArrayList<String> = arrayListOf<String>(),
    val vehicle_ownrship: String,
    val vehicles_for_spare_parts: String,
    val visit_key_id: String
)