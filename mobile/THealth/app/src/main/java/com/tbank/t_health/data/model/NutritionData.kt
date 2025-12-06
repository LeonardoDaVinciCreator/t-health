package com.tbank.t_health.data.model

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

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER
}