package com.cmrk.ui.activity.followup

data class FollowupResponseModel(
    val `data`: ArrayList<Data>,
    val message: String,
    val status: Int
)