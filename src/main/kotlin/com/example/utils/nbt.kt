package com.example.utils

import net.minecraft.nbt.NbtCompound


fun NbtCompound.getStringNullable(key: String): String? {
    return if (this.contains(key)) {
        this.getString(key)
    } else null
}

fun NbtCompound.getIntNullable(key: String): Int? {
    return if (this.contains(key)) {
        this.getInt(key)
    } else null
}