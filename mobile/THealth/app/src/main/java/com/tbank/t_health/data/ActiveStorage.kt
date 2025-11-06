package com.tbank.t_health.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.time.LocalDate

class ActiveStorage(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("active_minutes_prefs", Context.MODE_PRIVATE)

    private val KEY_MINUTES = "active_minutes"
    private val KEY_CALORIES = "active_calories"
    private val KEY_DATE = "active_date"

    private val KEY_SECONDS = "active_seconds"

    fun getActiveSeconds(): Int {
        val savedDate = prefs.getString(KEY_DATE, null)
        val today = LocalDate.now().toString()
        return if (savedDate == today) {
            prefs.getInt(KEY_SECONDS, 0)
        } else {
            resetDaily()
            0
        }
    }

    fun setActiveSeconds(seconds: Int) {
        prefs.edit()
            .putInt(KEY_SECONDS, seconds)
            .putString(KEY_DATE, LocalDate.now().toString())
            .apply()
    }

    fun getActiveMinutes(): Int {
        val savedDate = prefs.getString(KEY_DATE, null)
        val today = LocalDate.now().toString()

        return if (savedDate == today) {
            prefs.getInt(KEY_MINUTES, 0)
        } else {
            // Если день сменился — сбрасывается
            resetDaily()
            0
        }
    }

    fun setActiveMinutes(minutes: Int) {
        prefs.edit()
            .putInt(KEY_MINUTES, minutes)
            .putString(KEY_DATE, LocalDate.now().toString())
            .apply()
    }

    fun addActiveMinutes(minutes: Int) {
        val current = getActiveMinutes()
        val today = LocalDate.now().toString()
        val newTotal = current + minutes
        prefs.edit()
            .putInt(KEY_MINUTES, newTotal)
            .putString(KEY_DATE, today)
            .apply()

        Log.d("ActiveMinutesStorage", "Добавлено $minutes мин, всего: $newTotal")
    }

    fun getCalories(): Double {
        val savedDate = prefs.getString(KEY_DATE, null)
        val today = LocalDate.now().toString()
        return if (savedDate == today) prefs.getFloat(KEY_CALORIES, 0f).toDouble() else 0.0
    }

    fun setCalories(calories: Double) {
        prefs.edit()
            .putFloat(KEY_CALORIES, calories.toFloat())
            .putString(KEY_DATE, LocalDate.now().toString())
            .apply()
        Log.d("ActiveMinutesStorage", "Сохранено ${"%.1f".format(calories)} ккал активности")
    }

    fun addCalories(calories: Double) {
        val current = getCalories()
        val newTotal = current + calories
        prefs.edit()
            .putFloat(KEY_CALORIES, newTotal.toFloat())
            .putString(KEY_DATE, LocalDate.now().toString())
            .apply()
        Log.d("ActiveMinutesStorage", "Добавлено ${"%.1f".format(calories)} ккал, всего: ${"%.1f".format(newTotal)}")
    }

    /**
     * Обнулить минуты (в 23:59 или при смене дня)
     */
    fun resetDaily() {
        prefs.edit()
            .putInt(KEY_MINUTES, 0)
            .putFloat(KEY_CALORIES, 0f)
            .putString(KEY_DATE, LocalDate.now().toString())
            .apply()
        Log.d("ActiveMinutesStorage", "Сброшены активные минуты и калории")
    }


    fun getWorkoutMinutesForToday(): Int {
        val savedDate = prefs.getString(KEY_DATE, null)
        val today = LocalDate.now().toString()
        return if (savedDate == today) prefs.getInt(KEY_MINUTES, 0) else 0
    }

}
