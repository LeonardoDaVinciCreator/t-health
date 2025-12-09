package com.tbank.t_health.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tbank.t_health.data.model.LocalNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationStorage  @Inject constructor( @ApplicationContext private val  context: Context) {

    private val prefs =
        context.getSharedPreferences("notifications_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()
    private val KEY = "notifications"

    fun getAll(): List<LocalNotification> {
        val json = prefs.getString(KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<LocalNotification>>() {}.type
        return gson.fromJson(json, type)
    }

    fun add(notification: LocalNotification) {
        val updated = getAll() + notification
        save(updated)
    }


    fun save(list: List<LocalNotification>) {
        prefs.edit().putString(KEY, gson.toJson(list)).apply()
    }

    fun markAllAsRead() {
        val updated = getAll().map { it.copy(isRead = true) }
        save(updated)
    }

    fun hasUnread(): Boolean =
        getAll().any { !it.isRead }
}
