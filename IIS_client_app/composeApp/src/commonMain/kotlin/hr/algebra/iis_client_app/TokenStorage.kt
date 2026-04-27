package hr.algebra.iis_client_app

expect object TokenStorage {
    fun saveTokens(accessToken: String, refreshToken: String, role: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun getRole(): String?
    fun clear()
}