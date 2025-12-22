package com.tbank.t_health.ui.health.food

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tbank.t_health.R
import com.tbank.t_health.constants.NavigationDestinations
import com.tbank.t_health.data.model.MealType
import com.tbank.t_health.screens.formatDayHeader
import com.tbank.t_health.ui.theme.RobotoFontFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(
    navController: NavController,
    viewModel: NutritionViewModel = hiltViewModel()
) {
    val nutritions by viewModel.nutritions.collectAsState()
    val selectedRange by viewModel.selectedRange.collectAsState()


    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier.size(230.dp, 40.dp),
                onClick = {
                    navController.navigate(NavigationDestinations.ADD_NUTRITION)
                },
                containerColor = Color(0xFFFDD500),
                shape = RoundedCornerShape(11.dp),
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                text = {
                    Text(
                        "Добавить блюдо",
                        style = TextStyle(
                            fontFamily = RobotoFontFamily,
                            fontSize = 14.sp
                        )
                    )
                },
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_plus),
                        modifier = Modifier.size(11.dp),
                        contentDescription = "Plus",
                        colorFilter = ColorFilter.tint(Color(0xFF333333))
                    )
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            item {
                WeeklyNutritionStats(
                    nutritions = nutritions,
                    selectedDateRange = selectedRange,
                    onDateRangeSelected = viewModel::updateRange
                )
            }

            val groupedByDate = nutritions
                .filterByDateRange(selectedRange)
                .groupBy { it.parseDate() }
                .toSortedMap(compareByDescending { it })

            groupedByDate.forEach { (date, dayNutritions) ->

                item {
                    Text(
                        text = formatDayHeader(date),
                        modifier = Modifier.padding(start = 16.dp).padding(vertical = 4.dp),
                        style = TextStyle(
                            fontFamily = RobotoFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    )
                }

                MealType.values().forEach { mealType ->
                    val meals = dayNutritions.filter { it.mealType == mealType }
                    if (meals.isNotEmpty()) {
                        item {
                            MealBlock(mealType, meals)
                        }
                    }
                }
            }
        }
    }
}