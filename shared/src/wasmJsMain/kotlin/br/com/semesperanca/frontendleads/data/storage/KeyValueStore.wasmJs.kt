package br.com.semesperanca.frontendleads.data.storage

import kotlinx.browser.localStorage

actual fun createKeyValueStore(): KeyValueStore =
    try {
        LocalStorageKeyValueStore()
    } catch (_: Throwable) {
        InMemoryKeyValueStore()
    }

class LocalStorageKeyValueStore : KeyValueStore {

    override fun get(key: String): String? = localStorage.getItem(key)

    override fun set(key: String, value: String) {
        localStorage.setItem(key, value)
    }

    override fun remove(key: String) {
        localStorage.removeItem(key)
    }
}

class InMemoryKeyValueStore : KeyValueStore {

    private val map = HashMap<String, String>()

    override fun get(key: String): String? = map[key]

    override fun set(key: String, value: String) {
        map[key] = value
    }

    override fun remove(key: String) {
        map.remove(key)
    }
}