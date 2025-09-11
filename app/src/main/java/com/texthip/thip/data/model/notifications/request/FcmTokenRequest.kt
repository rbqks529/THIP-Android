package com.texthip.thip.data.model.notifications.request

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequest(
    val deviceId: String,
    val fcmToken: String,
    val platformType: String = "ANDROID"
)