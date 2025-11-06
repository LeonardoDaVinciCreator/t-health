package com.tbank.t_health.data

import java.time.LocalDate

data class HealthData(
    val date: LocalDate,
    val steps: Int = 0,
    val calories: Double = 0.0,
    val activeMinutes: Int = 0//минуты
)

fun HealthDataMonth() = listOf(
    HealthData(LocalDate.of(2025, 10, 1), steps = 7200, calories = 290.0, activeMinutes = 200),
    HealthData(LocalDate.of(2025, 10, 2), steps = 8100, calories = 325.0, activeMinutes = 50),
    HealthData(LocalDate.of(2025, 10, 3), steps = 6500, calories = 260.0, activeMinutes = 38),
    HealthData(LocalDate.of(2025, 10, 4), steps = 5400, calories = 215.0, activeMinutes = 30),
    HealthData(LocalDate.of(2025, 10, 5), steps = 4800, calories = 190.0, activeMinutes = 25),
    HealthData(LocalDate.of(2025, 10, 6), steps = 9300, calories = 370.0, activeMinutes = 55),
    HealthData(LocalDate.of(2025, 10, 7), steps = 10200, calories = 410.0, activeMinutes = 60),
    HealthData(LocalDate.of(2025, 10, 8), steps = 8700, calories = 350.0, activeMinutes = 48),
    HealthData(LocalDate.of(2025, 10, 9), steps = 7600, calories = 305.0, activeMinutes = 42),
    HealthData(LocalDate.of(2025, 10, 10), steps = 6900, calories = 275.0, activeMinutes = 40),
    HealthData(LocalDate.of(2025, 10, 11), steps = 5800, calories = 230.0, activeMinutes = 33),
    HealthData(LocalDate.of(2025, 10, 12), steps = 4900, calories = 195.0, activeMinutes = 28),
    HealthData(LocalDate.of(2025, 10, 13), steps = 11200, calories = 450.0, activeMinutes = 65),
    HealthData(LocalDate.of(2025, 10, 14), steps = 9800, calories = 390.0, activeMinutes = 57),
    HealthData(LocalDate.of(2025, 10, 15), steps = 8900, calories = 355.0, activeMinutes = 50),
    HealthData(LocalDate.of(2025, 10, 16), steps = 6700, calories = 270.0, activeMinutes = 37),
    HealthData(LocalDate.of(2025, 10, 17), steps = 9500, calories = 380.0, activeMinutes = 53),
    HealthData(LocalDate.of(2025, 10, 18), steps = 8600, calories = 345.0, activeMinutes = 47),
    HealthData(LocalDate.of(2025, 10, 19), steps = 9100, calories = 365.0, activeMinutes = 51),
    HealthData(LocalDate.of(2025, 10, 20), steps = 9700, calories = 390.0, activeMinutes = 55),
    HealthData(LocalDate.of(2025, 10, 21), steps = 7200, calories = 290.0, activeMinutes = 44),
    HealthData(LocalDate.of(2025, 10, 22), steps = 6500, calories = 260.0, activeMinutes = 38),
    HealthData(LocalDate.of(2025, 10, 23), steps = 8300, calories = 330.0, activeMinutes = 46),
    HealthData(LocalDate.of(2025, 10, 24), steps = 9100, calories = 365.0, activeMinutes = 52),
    HealthData(LocalDate.of(2025, 10, 25), steps = 7500, calories = 300.0, activeMinutes = 41),
    HealthData(LocalDate.of(2025, 10, 26), steps = 9800, calories = 395.0, activeMinutes = 56),
    HealthData(LocalDate.of(2025, 10, 27), steps = 10500, calories = 420.0, activeMinutes = 62),
    HealthData(LocalDate.of(2025, 10, 28), steps = 5000, calories = 200.0, activeMinutes = 29),
    HealthData(LocalDate.of(2025, 10, 29), steps = 6000, calories = 240.0, activeMinutes = 32),
    HealthData(LocalDate.of(2025, 10, 30), steps = 4000, calories = 160.0, activeMinutes = 20),
    HealthData(LocalDate.of(2025, 10, 31), steps = 7000, calories = 280.0, activeMinutes = 35)
)

fun List<HealthData>.toWeeklyGroups(): List<List<HealthData>> {
    return chunked(7)
}