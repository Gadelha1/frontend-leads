package br.com.semesperanca.frontendleads.data.storage

import java.util.prefs.Preferences

actual fun createKeyValueStore(): KeyValueStore = PreferencesKeyValueStore()

class PreferencesKeyValueStore : KeyValueStore {

    private val prefs = Preferences.userRoot().node("br.com.semesperanca.frontendleads")

    override fun get(key: String): String? = prefs.get(key, null)

    override fun set(key: String, value: String) {
        prefs.put(key, value)
        prefs.flush()
    }

    override fun remove(key: String) {
        prefs.remove(key)
        prefs.flush()
    }
}