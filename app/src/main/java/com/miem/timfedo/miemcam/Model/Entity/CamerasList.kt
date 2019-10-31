package com.miem.timfedo.miemcam.Model.Entity

import com.miem.timfedo.miemcam.Model.Entity.Objects.Camera
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.HashMapSerializer
import kotlinx.serialization.internal.StringSerializer
import kotlin.collections.ArrayList
import kotlinx.serialization.json.JSON

class CamerasList {

    var cameras = HashMap<String, ArrayList<Camera>>()
        private set

    fun addCamera(camera: Camera, toRoom: String) {
        val camerasInRoom = cameras[toRoom]
        if (camerasInRoom != null) {
            camerasInRoom.removeAll { it.uid == camera.uid }
            camerasInRoom.add(camera)
        } else {
            cameras[toRoom] = arrayListOf(camera)
        }
    }

    fun removeCamera(uid: String) {
        for ((_, camerasInRoom) in cameras) {
            camerasInRoom.removeAll { it.uid == uid }
        }
    }

    fun makeJson(): String {
        return JSON.stringify(HashMapSerializer(StringSerializer, ArrayListSerializer(Camera.serializer())), cameras)
    }

    fun parseJson(json: String): HashMap<String, ArrayList<Camera>> {
        return JSON.parse(HashMapSerializer(StringSerializer, ArrayListSerializer(Camera.serializer())), json) as HashMap<String, ArrayList<Camera>>
    }

    fun replaceAll(with: HashMap<String, ArrayList<Camera>>) {
        this.cameras = with
    }
}