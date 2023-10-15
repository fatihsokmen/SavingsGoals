package com.github.fatihsokmen.savingsgoals.core.auth

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokenStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    /**
     * Cache access token in memory whilst process is alive
     * and update once a new access token retrieved
     */
    private var accessToken: String? = null

    @WorkerThread
    fun getAccessToken(): String {
        if (accessToken == null) {
            accessToken = runBlocking {
                dataStore.data.map { preferences ->
                    preferences[KEY_ACCESS_TOKEN]
                }.first()
            }
        }
        return accessToken.orEmpty()
    }

    @WorkerThread
    fun putAccessToken(newToken: String) {
        // Wait for write in preferences
        runBlocking {
            dataStore.edit { preferences ->
                preferences[KEY_ACCESS_TOKEN] = newToken
            }
        }
        accessToken = newToken
    }

    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("auth.access-token")
    }
}