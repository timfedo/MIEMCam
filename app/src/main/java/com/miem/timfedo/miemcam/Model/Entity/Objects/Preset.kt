package com.miem.timfedo.miemcam.Model.Entity.Objects

import kotlinx.serialization.Serializable

@Serializable
data class Preset(val id: String, val name: String, val color: ArrayList<Int>)