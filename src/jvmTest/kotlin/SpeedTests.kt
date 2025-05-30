package com.sschr15.tests

import com.sschr15.NativeBackedIntArray
import com.sschr15.nativeBacked
import java.util.*
import kotlin.random.Random
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.time.measureTime

//const val ARRAY_SIZE = 10_000_000

// for extreme tests
const val ARRAY_SIZE = 1_000_000_000

@Ignore
class SpeedTests {
//    lateinit var normalArray: IntArray
//    lateinit var nativeArray: NativeBackedIntArray
//
//    @BeforeTest
//    fun setup() {
//        normalArray = IntArray(ARRAY_SIZE)
//        nativeArray = NativeBackedIntArray(ARRAY_SIZE)
//    }
//
//    @AfterTest
//    fun tearDown() {
//        normalArray = IntArray(0)
//        nativeArray.clear()
//    }

    val normalArray = IntArray(ARRAY_SIZE)
    val nativeArray = NativeBackedIntArray(ARRAY_SIZE)

    @Test
    fun `Array creation`() {
        val normalArrayTime = measureTime {
            IntArray(ARRAY_SIZE)
        }

        val nativeArrayTime = measureTime {
            NativeBackedIntArray(ARRAY_SIZE)
        }

        repeat(ARRAY_SIZE) {
            normalArray[it] = Random.nextInt()
        }

        val tempArray: NativeBackedIntArray
        val fromNormalArrayTime = measureTime {
            tempArray = normalArray.nativeBacked()
        }
        tempArray.close() // free the native memory

        val copyArrayTime = measureTime {
            normalArray.copyOf()
        }

        println("Normal array creation time: $normalArrayTime")
        println("Native backed array creation time: $nativeArrayTime")
        println("Normal array copy time: $copyArrayTime")
        println("""Native backed array "copy" time: $fromNormalArrayTime""")
    }

    @Test
    fun `Inserting elements`() {
        val normalArrayTime = measureTime {
            repeat(ARRAY_SIZE) {
                normalArray[it] = Random.nextInt()
            }
        }

        val nativeArrayTime = measureTime {
            repeat(ARRAY_SIZE) {
                nativeArray[it] = Random.nextInt()
            }
        }

        println("Normal array insertion time: $normalArrayTime")
        println("Native backed array insertion time: $nativeArrayTime")
    }

    @Test
    fun `Sorting elements`() {
        repeat(ARRAY_SIZE) {
            val value = Random.nextInt()
            normalArray[it] = value
            nativeArray[it] = value
        }

        val normalArrayTime = measureTime {
            normalArray.sort()
        }

        val nativeArrayTime = measureTime {
            nativeArray.sort()
        }

        println("Normal array sort time: $normalArrayTime")
        println("Native backed array sort time: $nativeArrayTime")
    }

    @Test
    fun `Equality checks`() {
        // Be warned: at extreme test, this will eat around 16 GB of memory

        repeat(ARRAY_SIZE) {
            val value = Random.nextInt()
            normalArray[it] = value
            nativeArray[it] = value
        }

        val normalB = normalArray.copyOf()
        val nativeB = normalB.nativeBacked()

        val normalEqualityTime = measureTime {
            assert(normalArray.contentEquals(normalB))
        }

        val nativeEqualityTime = measureTime {
            assert(nativeArray == nativeB)
        }

        println("Normal array equality time: $normalEqualityTime")
        println("Native backed array equality time: $nativeEqualityTime")
    }

    @Test
    fun `Array clearing`() {
        repeat(ARRAY_SIZE) {
            val value = Random.nextInt()
            normalArray[it] = value
            nativeArray[it] = value
        }

        val normalClearTime = measureTime {
            Arrays.fill(normalArray, 0)
        }

        val nativeClearTime = measureTime {
            nativeArray.clear()
        }

        println("Normal array clear time: $normalClearTime")
        println("Native backed array clear time: $nativeClearTime")
    }

    @Test
    fun `Array filling`() {
        repeat(ARRAY_SIZE) {
            val value = Random.nextInt()
            normalArray[it] = value
            nativeArray[it] = value
        }

        val normalArray = IntArray(ARRAY_SIZE)
        val nativeArray = NativeBackedIntArray(ARRAY_SIZE)

        val normalFillTime = measureTime {
            Arrays.fill(normalArray, 42)
        }

        val nativeFillTime = measureTime {
            nativeArray.fill(42)
        }

        println("Normal array fill time: $normalFillTime")
        println("Native backed array fill time: $nativeFillTime")
    }
}
