package com.tbank.t_health.ui.health.addFood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tbank.t_health.data.model.MealType
import com.tbank.t_health.ui.theme.RobotoFontFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  AddNutritionScreen(
    viewModel: AddNutritionViewModel = hiltViewModel(),
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var mealType by remember { mutableStateOf<MealType?>(null) }

    val fields = listOf(
        FoodFieldData("Название блюда:", name, { name = it }, "Булгур с овощами"),
        FoodFieldData("Калории:", calories, { calories = it }, "300", KeyboardType.Number, RoundedCornerShape(39.dp)),
        FoodFieldData("Белки:", protein, { protein = it }, "0", KeyboardType.Number),
        FoodFieldData("Жиры:", fats, { fats = it }, "0", KeyboardType.Number),
        FoodFieldData("Углеводы:", carbs, { carbs = it }, "0", KeyboardType.Number),
        FoodFieldData("Дата:", date, { date = it }, "дд.мм.гггг", shape = RoundedCornerShape(30.dp)),
        FoodFieldData("Тип приёма пищи:", mealType?.name ?: "Завтрак", {})
    )

    Scaffold(
    floatingActionButtonPosition = FabPosition.Center,
    floatingActionButton = {
        ExtendedFloatingActionButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(40.dp),
            onClick = {
                if (mealType != null && name.isNotBlank() && calories.isNotBlank() && date.isNotBlank()) {
                    viewModel.saveNutrition(
                        name, calories, protein, fats, carbs, date, mealType!!
                    )
                    navController.popBackStack()
                }
            },
            containerColor = Color(0xFFFDD500),
            shape = RoundedCornerShape(11.dp),
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Text(
                "Сохранить",
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontSize = 14.sp
                )
            )
        }
    },
    containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            FoodFields(
                fields = fields,
                selectedMealType = mealType,
                onMealTypeSelected = { mealType = it }
            )
        }
    }
}