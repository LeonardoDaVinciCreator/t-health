import android.content.Context
import com.tbank.t_health.data.UserData

class UserPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUser(userData: UserData) {
        prefs.edit()
            .putString("nickname", userData.nickname)
            .putString("fullName", userData.fullName)
            .putString("phone", userData.phone)
            .putString("code", userData.code)
            .apply()
    }

    fun getUser(): UserData? {
        val phone = prefs.getString("phone", null) ?: return null
        val nickname = prefs.getString("nickname", "") ?: ""
        val fullName = prefs.getString("fullName", "") ?: ""
        val code = prefs.getString("code", "") ?: ""
        return UserData(nickname, fullName, phone, code)
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.contains("phone")
    }

    fun clearUser() {
        prefs.edit().clear().apply()
    }
}
