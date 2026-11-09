package com.gamevault.mobile.data.datasource

interface SessionStore {
    fun getToken(): String?
    fun saveToken(token: String)
    fun clear()
}
