package com.miem.timfedo.miemcam.Model.Entity.Objects

import kotlinx.serialization.Serializable

@Serializable
data class Camera(val uid: String, val name: String, val room: String)