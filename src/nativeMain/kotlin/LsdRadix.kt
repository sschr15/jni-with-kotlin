@file:OptIn(ExperimentalForeignApi::class)

package com.sschr15

import kotlinx.cinterop.*
import platform.posix.memcpy
import platform.posix.memset

fun lsdRadixSort(array: CPointer<IntVar>) {
    val size = array[0]
    if (size <= 1) return

//    var max = array[1]
//    for (i in 2..size) {
//        if (array[i] > max) {
//            max = array[i]
//        }
//    }

    repeat(4) {
        val exp = 1 shl (it * 4) // 1, 16, 256, 4096
        countingSort(array, size, exp)
    }
}

private fun countingSort(array: CPointer<IntVar>, size: Int, exp: Int) = memScoped {
    val output = allocArray<IntVar>(size + 1)
    val count = allocArray<IntVar>(16)

    memset(count, 0, 16u * sizeOf<IntVar>().toULong())

    for (i in 1..size) {
        val index = (array[i] / exp) and 0xF
        count[index]++
    }

    for (i in 1..15) {
        count[i] += count[i - 1]
    }

    for (i in size downTo 1) {
        val index = (array[i] / exp) and 0xF
        output[count[index]--] = array[i]
    }

    memcpy(array, output, (size.toULong() + 1u) * sizeOf<IntVar>().toULong())
}

