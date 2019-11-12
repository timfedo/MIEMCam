package com.miem.timfedo.miemcam.Model.Entity

import com.miem.timfedo.miemcam.Model.Entity.Objects.Preset
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.NullableSerializer
import kotlinx.serialization.json.JSON

class PresetsList {

    var presets = ArrayList<Preset?>()
        private set

    init {
        for (i in 0..8) {
            presets.add(null)
        }
    }

    fun addPreset(preset: Preset, id: Int) {
        presets[id] = preset
    }

    fun removePreset(id: Int) {
        presets[id] = null
    }

    fun makeJson(): String {
        return JSON.stringify(ArrayListSerializer(NullableSerializer(Preset.serializer())), presets)
    }

    fun parseJson(json: String): ArrayList<Preset?> {
        return JSON.parse(ArrayListSerializer(NullableSerializer(Preset.serializer())), json) as ArrayList<Preset?>
    }

    fun replaceAll(with: ArrayList<Preset?>) {
        this.presets = with
    }
}