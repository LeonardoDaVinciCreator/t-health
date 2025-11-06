enum class WorkoutType(val displayName: String) {
    CARDIO("Кардио"),
    STRENGTH("Силовая"),
    ENDURANCE("На выносливость"),
    FLEXIBILITY("Гибкость"),
    BALANCE("Баланс");

    companion object {
        fun fromDisplayName(name: String): WorkoutType? =
            entries.firstOrNull { it.displayName == name }
    }
}
