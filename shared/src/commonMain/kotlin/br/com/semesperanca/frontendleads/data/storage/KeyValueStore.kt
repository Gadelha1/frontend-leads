package br.com.semesperanca.frontendleads.data.storage

interface KeyValueStore {
    fun get(key: String): String?
    fun set(key: String, value: String)
    fun remove(key: String)
}

expect fun createKeyValueStore(): KeyValueStore