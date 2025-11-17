import android.content.Context
import com.tbank.t_health.data.model.UserData

class UserPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUser(userData: UserData) {
        prefs.edit()
            .putLong("id", userData.id ?: -1L) // -1L если null
            .putString("username", userData.username)
            .putString("phone", userData.phone)
            .apply()
    }

    fun getUser(): UserData? {
        val id = prefs.getLong("id", -1L).takeIf { it != -1L }
        val username = prefs.getString("username", null) ?: return null
        val phone = prefs.getString("phone", null) ?: return null
        return UserData(
            id = id,
            username = username,
            phone = phone
        )
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.contains("username") && prefs.contains("phone")
    }

    fun clearUser() {
        prefs.edit().clear().apply()
    }
}
