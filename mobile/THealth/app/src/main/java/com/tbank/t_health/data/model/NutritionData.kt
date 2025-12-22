package com.tbank.t_health.data.model

import com.tbank.t_health.components.DisplayableType

data class NutritionGetData(
    val id: Long? = null,
    val userId: Long,
    val mealName: String,
    val mealCalories: Int,
    val mealType: MealType,
    val parameters: NutritionParameters,
    val date: String // ISO-формат: yyyy-MM-dd'T'HH:mm:ss
)

data class NutritionCreateData(
    val userId: Long,
    val mealName: String,
    val mealCalories: Int,
    val mealType: MealType,
    val parameters: NutritionParameters,
    val date: String // ISO-формат
)

data class NutritionParameters(
    val protein: Double,
    val fats: Double,
    val carbohydrate: Double
)

enum class MealType(override val displayName: String) : DisplayableType {
    BREAKFAST("Завтрак"),
    LUNCH("Обед"),
    DINNER("Ужин")
}

