import android.content.Context
import com.tbank.t_health.data.User

class UserPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        prefs.edit()
            .putString("nickname", user.nickname)
            .putString("fullName", user.fullName)
            .putString("phone", user.phone)
            .putString("code", user.code)
            .apply()
    }

    fun getUser(): User? {
        val phone = prefs.getString("phone", null) ?: return null
        val nickname = prefs.getString("nickname", "") ?: ""
        val fullName = prefs.getString("fullName", "") ?: ""
        val code = prefs.getString("code", "") ?: ""
        return User(nickname, fullName, phone, code)
    }

    fun isUserLoggedIn(): Boolean {
        return prefs.contains("phone")
    }

    fun clearUser() {
        prefs.edit().clear().apply()
    }
}
