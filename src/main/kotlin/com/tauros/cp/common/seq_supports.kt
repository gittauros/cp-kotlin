package com.tauros.cp.common

import com.tauros.cp.randomInt
import kotlin.math.abs


/**
 * @author tauros
 * 2023/9/9
 */
fun IntArray.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun LongArray.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun CharArray.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun <T> Array<T>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun IntArray.reverse(fromIndex: Int, toIndex: Int) {
    var (i, j) = fromIndex to toIndex - 1
    while (i < j) this.swap(i++, j--)
}

fun LongArray.reverse(fromIndex: Int, toIndex: Int) {
    var (i, j) = fromIndex to toIndex - 1
    while (i < j) this.swap(i++, j--)
}

fun CharArray.reverse(fromIndex: Int, toIndex: Int) {
    var (i, j) = fromIndex to toIndex - 1
    while (i < j) this.swap(i++, j--)
}

fun <T> Array<T>.reverse(fromIndex: Int, toIndex: Int) {
    var (i, j) = fromIndex to toIndex - 1
    while (i < j) this.swap(i++, j--)
}

fun <T> MutableList<T>.reverse(fromIndex: Int, toIndex: Int) {
    var (i, j) = fromIndex to toIndex - 1
    while (i < j) this.swap(i++, j--)
}

fun IntArray.nextPermutation(): Boolean {
    val upPos = (this.lastIndex downTo 0).first { it == 0 || this[it - 1] < this[it] } - 1
    val success = upPos >= 0
    if (upPos >= 0) {
        this.swap(upPos, (this.lastIndex downTo upPos + 1).first { this[it] > this[upPos] })
    }
    this.reverse(upPos + 1, this.size)
    return success
}

fun LongArray.nextPermutation(): Boolean {
    val upPos = (this.lastIndex downTo 0).first { it == 0 || this[it - 1] < this[it] } - 1
    val success = upPos >= 0
    if (upPos >= 0) {
        this.swap(upPos, (this.lastIndex downTo upPos + 1).first { this[it] > this[upPos] })
    }
    this.reverse(upPos + 1, this.size)
    return success
}

fun CharArray.nextPermutation(): Boolean {
    val upPos = (this.lastIndex downTo 0).first { it == 0 || this[it - 1] < this[it] } - 1
    val success = upPos >= 0
    if (upPos >= 0) {
        this.swap(upPos, (this.lastIndex downTo upPos + 1).first { this[it] > this[upPos] })
    }
    this.reverse(upPos + 1, this.size)
    return success
}

fun <T : Comparable<T>> Array<out T>.nextPermutation(): Boolean {
    val upPos = (this.lastIndex downTo 0).first { it == 0 || this[it - 1] < this[it] } - 1
    val success = upPos >= 0
    if (upPos >= 0) {
        this.swap(upPos, (this.lastIndex downTo upPos + 1).first { this[it] > this[upPos] })
    }
    this.reverse(upPos + 1, this.size)
    return success
}

fun <T : Comparable<T>> MutableList<out T>.nextPermutation(): Boolean {
    val upPos = (this.lastIndex downTo 0).first { it == 0 || this[it - 1] < this[it] } - 1
    val success = upPos >= 0
    if (upPos >= 0) {
        this.swap(upPos, (this.lastIndex downTo upPos + 1).first { this[it] > this[upPos] })
    }
    this.reverse(upPos + 1, this.size)
    return success
}