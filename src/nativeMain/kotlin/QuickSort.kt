@file:OptIn(ExperimentalForeignApi::class)

package com.sschr15

import kotlinx.cinterop.*

// apparently kotlin uses quick sort for integer arrays
// if it's good enough for jetbrains, it's good enough for me
fun quickSort(array: CPointer<IntVar>) {
    val size = array[0]
    if (size <= 1) return
    quickSort(array, 1, size)
}

private fun quickSort(array: CPointer<IntVar>, low: Int, high: Int) {
    if (low < high) {
        val pi = partition(array, low, high)
        quickSort(array, low, pi - 1)
        quickSort(array, pi + 1, high)
    }
}

private fun partition(array: CPointer<IntVar>, low: Int, high: Int): Int {
    var i = low
    var j = high
    val pivot = array[(low + high) / 2]
    while (i <= j) {
        while (array[i] < pivot) i++
        while (array[j] > pivot) j--
        if (i <= j) {
            array.swap(i, j)
            i++
            j--
        }
    }
    return i
}

@Suppress("NOTHING_TO_INLINE")
private inline fun CPointer<IntVar>.swap(a: Int, b: Int) {
    val temp = this[a]
    this[a] = this[b]
    this[b] = temp
}
