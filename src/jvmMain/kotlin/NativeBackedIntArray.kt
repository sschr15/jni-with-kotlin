package com.sschr15

fun IntArray.nativeBacked(): NativeBackedIntArray =
    NativeBackedIntArray(this)
