@file:OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
@file:Suppress("unused")

package com.sschr15

import kotlinx.cinterop.*
import platform.jni.*
import platform.posix.*
import kotlin.experimental.ExperimentalNativeApi

private const val PREFIX = "Java_com_sschr15_NativeBackedIntArray"

@JniExport
@CName("${PREFIX}_createNativeArray")
fun createNativeArray(env: CPointer<JNIEnvVar>, cls: jclass, size: jint): jlong {
    val nativeArray = calloc(size.toULong() + 1uL, sizeOf<IntVar>().toULong())
    if (nativeArray == null) {
        throw OutOfMemoryError("Failed to allocate native array of size $size")
    }

    nativeArray.reinterpret<IntVar>()[0] = size
    return nativeArray.toLong()
}

@JniExport
@CName("${PREFIX}_createFromArray")
fun createFromArray(env: CPointer<JNIEnvVar>, cls: jclass, array: jintArray): jlong {
    val size = env.pointed.pointed!!.GetArrayLength!!(env, array)
    val nativeArray = malloc((size + 1).toULong() * sizeOf<IntVar>().toULong())
    if (nativeArray == null) {
        throw OutOfMemoryError("Failed to allocate native array of size $size")
    }
    val asIntArray = nativeArray.reinterpret<IntVar>()
    asIntArray[0] = size
    env.pointed.pointed!!.GetIntArrayRegion!!(env, array, 0, size, asIntArray + 1)
    return nativeArray.toLong()
}

@JniExport
@CName("${PREFIX}_equals")
fun equals(env: CPointer<JNIEnvVar>, cls: jclass, ptr1: jlong, ptr2: jlong): jboolean {
    val array1 = ptr1.toCPointer<IntVar>()!!
    val array2 = ptr2.toCPointer<IntVar>()!!

    val size1 = array1[0]
    val size2 = array2[0]

    if (size1 != size2) return JNI_FALSE.toUByte()

    val comparison = memcmp(array1 + 1, array2 + 1, size1.toULong() * sizeOf<IntVar>().toULong())
    return (if (comparison == 0) JNI_TRUE else JNI_FALSE).toUByte()
}

@JniExport
@CName("${PREFIX}_get")
fun get(env: CPointer<JNIEnvVar>, cls: jclass, ptr: jlong, index: jint): jint {
    val array = ptr.toCPointer<IntVar>()!!
//    val size = array[0]
//    if (index < 0 || index >= size) {
//        throw IndexOutOfBoundsException("Index $index out of bounds for size $size")
//    }
    return array[index + 1]
}

@JniExport
@CName("${PREFIX}_set")
fun set(env: CPointer<JNIEnvVar>, cls: jclass, ptr: jlong, index: jint, value: jint) {
    val array = ptr.toCPointer<IntVar>()!!
//    val size = array[0]
//    if (index < 0 || index >= size) {
//        throw IndexOutOfBoundsException("Index $index out of bounds for size $size")
//    }
    array[index + 1] = value
}

@JniExport
@CName("${PREFIX}_size")
fun size(env: CPointer<JNIEnvVar>, cls: jclass, ptr: jlong): jint {
    val array = ptr.toCPointer<IntVar>()!!
    return array[0]
}

@JniExport
@CName("${PREFIX}_clear")
fun clear(env: CPointer<JNIEnvVar>, cls: jclass, ptr: jlong) {
    val array = ptr.toCPointer<IntVar>()!!
    val size = array[0]
    memset(array + 1, 0, size.toULong() * sizeOf<IntVar>().toULong())
}

@JniExport
@CName("${PREFIX}_fill")
fun fill(env: CPointer<JNIEnvVar>, cls: jclass, ptr: jlong, value: jint) {
    val array = ptr.toCPointer<IntVar>()!!
    val size = array[0]
    for (i in 1..size) {
        array[i] = value
    }
}

@JniExport
@CName("${PREFIX}_sort")
fun sort(env: CPointer<JNIEnvVar>, cls: jclass, ptr: jlong) {
    val array = ptr.toCPointer<IntVar>()!!
    val size = array[0]
    if (size > 1) {
        lsdRadixSort(array)
//        quickSort(array)
    }
}

@JniExport
@CName("${PREFIX}_deleteNativeArray")
fun deleteNativeArray(env: CPointer<JNIEnvVar>, cls: jclass, ptr: jlong) {
    free(ptr.toCPointer<COpaquePointerVar>()!!)
}
