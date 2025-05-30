package com.sschr15.tests

import com.sschr15.NativeBackedIntArray
import com.sschr15.nativeBacked
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class Tests {
    @Test
    fun `Single array operations`() {
        NativeBackedIntArray(5).use { array ->
            array[0] = 10
            assertEquals(10, array[0])
            assertEquals(5, array.size())
        }
    }

    @Test
    fun `Array equality`() {
        NativeBackedIntArray(5).use { array ->
            array[0] = 10

            val anotherArray = NativeBackedIntArray(5)
            anotherArray[0] = 10
            assertEquals(array, anotherArray)
        }
    }

    @Test
    fun `Array modification`() {
        NativeBackedIntArray(1000).use { array ->
            array.fill(42)
            for (i in 0..<1000) {
                assertEquals(42, array[i])
            }

            array.clear()
            for (i in 0..<1000) {
                assertEquals(0, array[i])
            }
        }
    }

    @Test
    fun `Array sorting`() {
        val randomNumbers = IntArray(1000) { Random.nextInt(1000) }
        val nativeArray = randomNumbers.nativeBacked()
        nativeArray.sort()
        randomNumbers.sort()
        val secondArray = randomNumbers.nativeBacked()
        assertEquals(secondArray, nativeArray)
    }
}
