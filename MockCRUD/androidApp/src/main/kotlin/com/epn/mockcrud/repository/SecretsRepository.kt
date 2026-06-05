package com.epn.mockcrud.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "datastore_prefs")

class SecretsRepository(private val context: Context) {

    // --- SharedPreferences simple ---
    private val sharedPrefs = context.getSharedPreferences("simple_prefs", Context.MODE_PRIVATE)

    fun saveSharedPref(key: String, value: String) =
        sharedPrefs.edit().putString(key, value).apply()

    fun getSharedPref(key: String): String? = sharedPrefs.getString(key, null)

    // --- DataStore ---
    suspend fun saveDataStore(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { it[prefKey] = value }
    }

    suspend fun getDataStore(key: String): String? {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { it[prefKey] }.first()
    }

    // --- EncryptedSharedPreferences ---
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs = EncryptedSharedPreferences.create(
        context,
        "secret_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveEncrypted(key: String, value: String) =
        encryptedPrefs.edit().putString(key, value).apply()

    fun getEncrypted(key: String): String? = encryptedPrefs.getString(key, null)
}
