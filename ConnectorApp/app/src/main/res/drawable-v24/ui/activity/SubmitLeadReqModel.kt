package com.cmrk.ui.activity

import java.io.Serializable


data class SubmitLeadReqModel(
    var employeeKeyId: String? = "",
    var tripKeyId: String? = "",
    var make: String? = "",
    var model: String? = "",
    var vehicleId: String? = "",
    var manufactureYear: String? = "",
    var registrationNumber: String? = "",
    var kerbWeight: String? = "",
    var vehicleOwnrship: String? = "",
    var vehicleCondition: String? = "",
    var fuelType: String? = "",
    var tankCapacity: String? = "",
    var tankExpiryDate: String? = "",
    var towingCharge: String? = "",
    var expectedAmount: String? = "",
    var orcAvailable: String? = "",
    var vehicleImages: ArrayList<String> = arrayListOf<String>(),
    var sparePartRequirement: String? = null,
    var vehiclesForSpareParts: String? = null,
    var specificSparePart: String? = null,
    var immediateRequirement: String? = null,
    var priceExpectationPerKg: String? = null,
    var dailyOfferInterest: String? = null,
    var visit_key_id:String?=""

) : Serializable
