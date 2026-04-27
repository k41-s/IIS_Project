package hr.algebra.iis_client_app

import java.util.prefs.Preferences

actual object TokenStorage {
    private val prefs = Preferences.userRoot().node("hr.algebra.iis_client_app.auth")

    actual fun saveTokens(accessToken: String, refreshToken: String, role: String) {
        prefs.put("ACCESS_TOKEN", accessToken)
        prefs.put("REFRESH_TOKEN", refreshToken)
        prefs.put("ROLE", role)
    }

    actual fun getAccessToken(): String? = prefs.get("ACCESS_TOKEN", null)
    actual fun getRefreshToken(): String? = prefs.get("REFRESH_TOKEN", null)
    actual fun getRole(): String? = prefs.get("ROLE", null)

    actual fun clear() {
        prefs.clear()
    }
}