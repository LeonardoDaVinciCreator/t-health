package com.tbank.t_health.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import com.tbank.t_health.data.model.WorkoutData
import com.tbank.t_health.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WorkoutRepository(private val context: Context) {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, object : JsonSerializer<LocalDate>,
            JsonDeserializer<LocalDate> {
            private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

            override fun serialize(
                src: LocalDate?,
                typeOfSrc: Type?,
                context: JsonSerializationContext?
            ): JsonElement {
                return JsonPrimitive(src?.format(formatter))
            }

            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): LocalDate {
                return LocalDate.parse(json?.asString, formatter)
            }
        })
        .create()
    private val workoutDataFile = "workout_data.json"  // файл хранения
    private fun getFile(): File = File(context.filesDir, workoutDataFile)

    suspend fun saveWorkoutLocally(workout: WorkoutData) = withContext(Dispatchers.IO) {
        val file = getFile()
        val existingList = if (file.exists()) {
            val json = file.readText()
            gson.fromJson<List<WorkoutData>>(json, object : TypeToken<List<WorkoutData>>() {}.type)
        } else emptyList()

        val updatedList = existingList + workout
        file.writeText(gson.toJson(updatedList))
        Log.d("WorkoutRepository", "Workout saved locally: ${workout.name} (${workout.plannedDate})")
    }

    suspend fun loadLocalWorkouts(): List<WorkoutData> = withContext(Dispatchers.IO) {
        val file = getFile()
        Log.d("WorkoutRepository", "Attempting to load local workouts. File exists: ${file.exists()}")
        if (!file.exists()) return@withContext emptyList()
        val json = file.readText()
        Log.d("WorkoutRepository", "Read json data length: ${json.length}")
        Log.d("WorkoutRepository", "Read json data: $json")
        val workouts = gson.fromJson<List<WorkoutData>>(json, object : TypeToken<List<WorkoutData>>() {}.type)
        Log.d("WorkoutRepository", "Loaded workouts count: ${workouts.size}")
        workouts
    }
    suspend fun clearCorruptedData() = withContext(Dispatchers.IO) {
        val file = getFile()
        if (file.exists()) {
            file.delete()
            Log.d("WorkoutRepository", "Corrupted data cleared")
        }
    }

    suspend fun syncToServer() = withContext(Dispatchers.IO) {
        try {
            val workouts = loadLocalWorkouts()
            for (workout in workouts) {
                RetrofitInstance.api.postWorkout(workout)
                Log.d(
                    "WorkoutRepository",
                    "Synced workout ${workout.id} (${workout.name}) to server"
                )
            }
        } catch (e: Exception) {
            Log.e("WorkoutRepository", "Sync error: ${e.message}")
        }
    }

    suspend fun markWorkoutCompleted(id: Long) = withContext(Dispatchers.IO) {
        val file = getFile()
        if (!file.exists()) return@withContext
        val json = file.readText()
        val workouts =
            gson.fromJson<List<WorkoutData>>(json, object : TypeToken<List<WorkoutData>>() {}.type)

        val updated = workouts.map {
            if (it.id == id) it.copy(isCompleted = true) else it
        }

        file.writeText(gson.toJson(updated))
        Log.d("WorkoutRepository", "Workout $id marked as completed")
    }

    suspend fun clearLocalData() = withContext(Dispatchers.IO) {
        val file = getFile()
        if (file.exists()) {
            file.delete()
            Log.d("WorkoutRepository", "Local workout data cleared")
        }
    }
}