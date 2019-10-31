package com.miem.timfedo.miemcam.Model

import kotlinx.serialization.internal.FloatSerializer
import kotlinx.serialization.internal.HashMapSerializer
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.JSON

class Settings {

    var settings = HashMap<String, HashMap<String, Float>>()
        private set

    fun addSetting(type: String, key: String, value: Float) {
        settings[type]?.put(key, value)
    }

    fun removeSetting(type: String, key: String) {
        settings[type]?.remove(key)
    }

    fun makeJson(): String {
        return JSON.stringify(HashMapSerializer(StringSerializer, HashMapSerializer(StringSerializer, FloatSerializer)), settings)
    }

    fun parseJson(json: String): HashMap<String, HashMap<String, Float>> {
        return JSON.parse(HashMapSerializer(StringSerializer, HashMapSerializer(StringSerializer, FloatSerializer)), json) as HashMap<String, HashMap<String, Float>>
    }

    fun replaceAll(with: HashMap<String, HashMap<String, Float>>) {
        this.settings = with
    }
}