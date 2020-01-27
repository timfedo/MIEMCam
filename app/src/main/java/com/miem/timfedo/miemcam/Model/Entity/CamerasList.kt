package com.miem.timfedo.miemcam.Model.Entity

import com.miem.timfedo.miemcam.Model.Entity.Objects.Camera
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.HashMapSerializer
import kotlinx.serialization.internal.StringSerializer
import kotlin.collections.ArrayList
import kotlinx.serialization.json.JSON

class CamerasList {

    companion object {
        fun makeJson(camerasList: CamerasList): String {
            return JSON.stringify(ArrayListSerializer(Camera.serializer()), camerasList.cameras)
        }

        fun parseJson(json: String): ArrayList<Camera> {
            return JSON.parse(ArrayListSerializer(Camera.serializer()), json) as ArrayList<Camera>
        }
    }

    var cameras = ArrayList<Camera>()
        private set

    fun addCamera(camera: Camera) {
        cameras.removeAll { it.uid == camera.uid }
        cameras.add(camera)
    }

    fun removeCamera(uid: String) {
        cameras.removeAll { it.uid == uid }
    }

    fun replaceAll(with: ArrayList<Camera>) {
        this.cameras = with
    }
}