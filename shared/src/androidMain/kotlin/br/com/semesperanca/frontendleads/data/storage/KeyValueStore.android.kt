package br.com.semesperanca.frontendleads.data.storage

import android.content.Context
import br.com.semesperanca.frontendleads.AndroidContextHolder

actual fun createKeyValueStore(): KeyValueStore = AndroidKeyValueStore(
    AndroidContextHolder.appContext,
)

class AndroidKeyValueStore(context: Context) : KeyValueStore {

    private val prefs = context.getSharedPreferences("mobile_leads_prefs", Context.MODE_PRIVATE)

    override fun get(key: String): String? = prefs.getString(key, null)

    override fun set(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}