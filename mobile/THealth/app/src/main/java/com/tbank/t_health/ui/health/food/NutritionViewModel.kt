package com.tbank.t_health.ui.health.food

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.t_health.data.model.NutritionGetData
import com.tbank.t_health.data.repository.NutritionRepository
import com.tbank.t_health.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val repository: NutritionRepository,
    private val getUserUseCase: GetUserUseCase
) : ViewModel(){
    private val _nutritions = MutableStateFlow<List<NutritionGetData>>(emptyList())
    val nutritions: StateFlow<List<NutritionGetData>> = _nutritions.asStateFlow()

    private val _selectedRange = MutableStateFlow<Pair<LocalDate?, LocalDate?>?>(null)
    val selectedRange: StateFlow<Pair<LocalDate?, LocalDate?>?> = _selectedRange.asStateFlow()
    
    init {
        loadNutritions()
    }
    private fun loadNutritions() {
        viewModelScope.launch {
            try {
                val user = getUserUseCase()
                if (user?.id != null) {
                    _nutritions.value = repository.getUserNutritions(user.id)
                }
            } catch (e: Exception) {
                Log.e("NutritionVM", "Ошибка загрузки", e)
            }
        }
    }

    fun updateRange(start: LocalDate?, end: LocalDate?) {
        _selectedRange.value = start to end
    }
}