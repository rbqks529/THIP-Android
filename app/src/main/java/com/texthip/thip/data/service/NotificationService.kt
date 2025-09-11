package com.texthip.thip.data.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.notifications.request.FcmTokenRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationService {
    @POST("notifications/fcm-tokens")
    suspend fun registerFcmToken(
        @Body request: FcmTokenRequest
    ): BaseResponse<Unit>
}