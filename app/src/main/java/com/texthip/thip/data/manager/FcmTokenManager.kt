package com.texthip.thip.data.manager

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.texthip.thip.data.repository.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenManager @Inject constructor(
    private val tokenManager: TokenManager,
    private val notificationRepository: NotificationRepository,
    private val context: Context
) {

    suspend fun handleNewToken(newToken: String) {
        val storedToken = tokenManager.getFcmTokenOnce()
        
        if (storedToken != newToken) {
            Log.d("FCM", "Token updated")
            
            // 새 토큰 저장
            tokenManager.saveFcmToken(newToken)
            
            // 서버에 전송
            sendTokenToServer(newToken)
        }
    }

    suspend fun sendCurrentTokenIfExists() {
        val storedFcmToken = tokenManager.getFcmTokenOnce()
        
        if (storedFcmToken != null) {
            sendTokenToServer(storedFcmToken)
        } else {
            // 저장된 토큰이 없으면 Firebase에서 직접 가져와서 저장하고 전송
            fetchAndSendCurrentToken()
        }
    }

    private fun fetchAndSendCurrentToken() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Failed to fetch token", task.exception)
                    return@addOnCompleteListener
                }

                val token = task.result
                // 토큰을 저장하고 서버로 전송 (비동기)
                CoroutineScope(Dispatchers.IO).launch {
                    tokenManager.saveFcmToken(token)
                    sendTokenToServer(token)
                }
            }
        } catch (e: Exception) {
            Log.e("FCM", "Error fetching FCM token", e)
        }
    }

    private suspend fun sendTokenToServer(token: String) {
        runCatching {
            val deviceId = getDeviceId()
            notificationRepository.registerFcmToken(deviceId, token)
        }.onSuccess {
            it.onSuccess {
                Log.d("FCM", "Token sent successfully")
            }.onFailure { exception ->
                Log.e("FCM", "Failed to send token", exception)
            }
        }.onFailure { exception ->
            Log.e("FCM", "Error sending token", exception)
        }
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceId(): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown_device"
    }
}