package com.texthip.thip.data.repository

import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.notifications.request.FcmTokenRequest
import com.texthip.thip.data.service.NotificationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val notificationService: NotificationService
) {
    suspend fun registerFcmToken(
        deviceId: String,
        fcmToken: String
    ): Result<Unit> {
        return runCatching {
            val request = FcmTokenRequest(
                deviceId = deviceId,
                fcmToken = fcmToken,
                platformType = "ANDROID"
            )
            val response = notificationService.registerFcmToken(request)
            response.handleBaseResponse().getOrThrow()
        }
    }
}