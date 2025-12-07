package com.tbank.t_health.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tbank.t_health.data.model.LocalNotification
import com.tbank.t_health.data.model.UserData
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class UserPrefs @Inject constructor(@ApplicationContext context: Context) {


    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()
    fun saveUser(userData: UserData) {
        prefs.edit()
            .putLong("id", userData.id ?: -1L) // -1L если null
            .putString("username", userData.username)
            .putString("phone", userData.phone)
            .apply()
    }

    fun getUser(): UserData? {
        val id = prefs.getLong("id", -1L).takeIf { it != -1L }
        val username = prefs.getString("username", null) ?: return null
        val phone = prefs.getString("phone", null) ?: return null
        return UserData(
            id = id,
            username = username,
            phone = phone
        )
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.contains("username") && prefs.contains("phone")
    }

    fun clearUser() {
        prefs.edit().clear().apply()
    }

    // === Уведомления ===
    private fun getNotificationsKey(): String = "notifications"

    fun saveNotifications(notifications: List<LocalNotification>) {
        val json = gson.toJson(notifications)
        prefs.edit().putString(getNotificationsKey(), json).apply()
    }

    fun getNotifications(): List<LocalNotification> {
        val json = prefs.getString(getNotificationsKey(), "[]") ?: "[]"
        return gson.fromJson(json, object : TypeToken<List<LocalNotification>>() {}.type)
    }

    fun hasUnreadNotifications(): Boolean {
        return getNotifications().any { !it.isRead }
    }

    // (опционально) пометить все как прочитанные
    fun markAllNotificationsAsRead() {
        val updated = getNotifications().map { it.copy(isRead = true) }
        saveNotifications(updated)
    }

    // (опционально) добавить одно уведомление
    fun addNotification(notification: LocalNotification) {
        val list = getNotifications().toMutableList()
        list.add(notification)
        saveNotifications(list)
    }
}