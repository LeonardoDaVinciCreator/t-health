package com.tbank.t_health.ui.health.addFood

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.t_health.data.model.MealType
import com.tbank.t_health.data.model.NutritionCreateData
import com.tbank.t_health.data.model.NutritionParameters
import com.tbank.t_health.data.repository.NutritionRepository
import com.tbank.t_health.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@HiltViewModel
class AddNutritionViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository,
    private val getUserUseCase: GetUserUseCase
) : ViewModel(){
    fun saveNutrition(
        name: String,
        calories: String,
        protein: String,
        fats: String,
        carbs: String,
        date: String,
        mealType: MealType
    ) {
        val user = getUserUseCase() ?: return
        val userId = user.id ?: return

        val isoDate = try {
            LocalDate
                .parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                .atStartOfDay()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        } catch (e: Exception) {
            Log.e("NUTRITION", "Invalid date", e)
            return
        }

        viewModelScope.launch {
            try {
                nutritionRepository.createNutrition(
                    NutritionCreateData(
                        userId = userId,
                        mealName = name,
                        mealCalories = calories.toInt(),
                        mealType = mealType,
                        parameters = NutritionParameters(
                            protein = protein.toDoubleOrNull() ?: 0.0,
                            fats = fats.toDoubleOrNull() ?: 0.0,
                            carbohydrate = carbs.toDoubleOrNull() ?: 0.0
                        ),
                        date = isoDate
                    )
                )
            } catch (e: Exception) {
                Log.e("NUTRITION", "Error saving nutrition", e)
            }
        }
    }
}