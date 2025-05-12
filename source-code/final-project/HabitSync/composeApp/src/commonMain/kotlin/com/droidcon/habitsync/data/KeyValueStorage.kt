package com.droidcon.habitsync.data

interface KeyValueStorage {
    suspend fun getString(key: String, default: String): String
    suspend fun setString(key: String, value: String)
}
