package com.tauros.cp

import com.tauros.cp.common.MIntArray
import com.tauros.cp.structure.AVLMap
import com.tauros.cp.structure.AVLSet
import kotlin.math.abs


/**
 * @author tauros
 * 2023/8/28
 */
public inline val Char.code: Int get() = this.toInt()

// reduce
public inline fun IntArray.runningReduce(operation: (acc: Int, Int) -> Int): List<Int> {
    if (isEmpty()) return emptyList()
    var accumulator = this[0]
    val result = ArrayList<Int>(size).apply { add(accumulator) }
    for (index in 1 until size) {
        accumulator = operation(accumulator, this[index])
        result.add(accumulator)
    }
    return result
}

public inline fun LongArray.runningReduce(operation: (acc: Long, Long) -> Long): List<Long> {
    if (isEmpty()) return emptyList()
    var accumulator = this[0]
    val result = ArrayList<Long>(size).apply { add(accumulator) }
    for (index in 1 until size) {
        accumulator = operation(accumulator, this[index])
        result.add(accumulator)
    }
    return result
}

public inline fun FloatArray.runningReduce(operation: (acc: Float, Float) -> Float): List<Float> {
    if (isEmpty()) return emptyList()
    var accumulator = this[0]
    val result = ArrayList<Float>(size).apply { add(accumulator) }
    for (index in 1 until size) {
        accumulator = operation(accumulator, this[index])
        result.add(accumulator)
    }
    return result
}

public inline fun DoubleArray.runningReduce(operation: (acc: Double, Double) -> Double): List<Double> {
    if (isEmpty()) return emptyList()
    var accumulator = this[0]
    val result = ArrayList<Double>(size).apply { add(accumulator) }
    for (index in 1 until size) {
        accumulator = operation(accumulator, this[index])
        result.add(accumulator)
    }
    return result
}

// fold
public inline fun <R> IntArray.runningFold(initial: R, operation: (acc: R, Int) -> R): List<R> {
    if (isEmpty()) return listOf(initial)
    val result = ArrayList<R>(size + 1).apply { add(initial) }
    var accumulator = initial
    for (element in this) {
        accumulator = operation(accumulator, element)
        result.add(accumulator)
    }
    return result
}

public inline fun <R> LongArray.runningFold(initial: R, operation: (acc: R, Long) -> R): List<R> {
    if (isEmpty()) return listOf(initial)
    val result = ArrayList<R>(size + 1).apply { add(initial) }
    var accumulator = initial
    for (element in this) {
        accumulator = operation(accumulator, element)
        result.add(accumulator)
    }
    return result
}

public inline fun <R> FloatArray.runningFold(initial: R, operation: (acc: R, Float) -> R): List<R> {
    if (isEmpty()) return listOf(initial)
    val result = ArrayList<R>(size + 1).apply { add(initial) }
    var accumulator = initial
    for (element in this) {
        accumulator = operation(accumulator, element)
        result.add(accumulator)
    }
    return result
}

public inline fun <R> DoubleArray.runningFold(initial: R, operation: (acc: R, Double) -> R): List<R> {
    if (isEmpty()) return listOf(initial)
    val result = ArrayList<R>(size + 1).apply { add(initial) }
    var accumulator = initial
    for (element in this) {
        accumulator = operation(accumulator, element)
        result.add(accumulator)
    }
    return result
}

inline fun iao(vararg nums: Int) = intArrayOf(*nums)
inline fun lao(vararg nums: Long) = longArrayOf(*nums)
inline fun bao(vararg vals: Boolean) = booleanArrayOf(*vals)
inline fun dao(vararg nums: Double) = doubleArrayOf(*nums)
inline fun cao(vararg nums: Char) = charArrayOf(*nums)

inline fun <reified T> ao(vararg elements: T) = arrayOf(*elements)
inline fun miao(vararg nums: Int) = miar(nums.size) { nums[it] }

inline fun iar(size: Int, init: (Int) -> Int = { 0 }) = IntArray(size) { init(it) }
inline fun lar(size: Int, init: (Int) -> Long = { 0L }) = LongArray(size) { init(it) }
inline fun bar(size: Int, init: (Int) -> Boolean = { false }) = BooleanArray(size) { init(it) }
inline fun dar(size: Int, init: (Int) -> Double = { 0.0 }) = DoubleArray(size) { init(it) }
inline fun car(size: Int, init: (Int) -> Char = { Char(0) }) = CharArray(size) { init(it) }

inline fun <reified T> ar(size: Int, init: (Int) -> T) = Array(size) { init(it) }
inline fun miar(size: Int, crossinline init: (Int) -> Int = { 0 }) = MIntArray(size) { init(it) }

inline fun <reified T> lo(vararg elements: T) = listOf(*elements)
inline fun <reified T> mlo(vararg elements: T) = mutableListOf(*elements)

inline fun <reified T> so(vararg elements: T) = setOf(*elements)
inline fun <reified T> mso(vararg elements: T) = mutableSetOf(*elements)

inline fun <reified T> avls(cmp: Comparator<T>) = AVLSet(cmp)
inline fun <reified K, reified V> avlm(cmp: Comparator<K>) = AVLMap<K, V>(cmp)

inline fun <reified K, reified V> mo(vararg pairs: Pair<K, V>) = mapOf(*pairs)
inline fun <reified K, reified V> mmo(vararg pairs: Pair<K, V>) = mutableMapOf(*pairs)

inline fun <reified T> dq() = ArrayDeque<T>()

fun randomInt(): Int {
    var num = -1
    repeat(abs((System.nanoTime() % 11).toInt()) + 1) {
        num = num xor System.nanoTime().toInt()
    }
    return num
}