package com.gamevault.mobile.data.datasource

import android.content.Context

class SharedPreferencesSessionStore(context: Context) : SessionStore {
    private val preferences = context.applicationContext.getSharedPreferences(
        "gamevault_session", Context.MODE_PRIVATE
    )
    override fun getToken(): String? = preferences.getString("jwt_token", null)
    override fun saveToken(token: String) {
        preferences.edit().putString("jwt_token", token).apply()
    }
    override fun clear() { preferences.edit().remove("jwt_token").apply() }
}
