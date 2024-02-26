package com.tauros.cp.structure


/**
 * @author tauros
 * 2023/9/12
 */
inline fun IntArray.bitInitWithIndex(operation: IntArray.(Int, Int) -> Int) {
    for (i in 1 until size) {
        val next = i + i.takeLowestOneBit()
        if (next < size) {
            this[next] = operation(i, next)
        }
    }
}

inline fun IntArray.bitUpdateWithIndex(pos: Int, calc: IntArray.(Int) -> Unit) {
    if (pos <= 0) return
    var iter = pos
    while (iter < size) {
        calc(iter)
        iter += iter.takeLowestOneBit()
    }
}

inline fun <R> IntArray.bitQueryWithIndex(pos: Int, initial: R, operation: IntArray.(R, Int) -> R): R {
    var iter = pos
    var ans = initial
    while (iter > 0) {
        ans = operation(ans, iter)
        iter -= iter.takeLowestOneBit()
    }
    return ans
}

inline fun <R> IntArray.bitQuery(pos: Int, initial: R, operation: (R, Int) -> R): R {
    var iter = pos
    var ans = initial
    while (iter > 0) {
        ans = operation(ans, this[iter])
        iter -= iter.takeLowestOneBit()
    }
    return ans
}

inline fun LongArray.bitInitWithIndex(operation: LongArray.(Int, Int) -> Long) {
    for (i in 1 until size) {
        val next = i + i.takeLowestOneBit()
        if (next < size) {
            this[next] = operation(i, next)
        }
    }
}

inline fun LongArray.bitUpdateWithIndex(pos: Int, calc: LongArray.(Int) -> Unit) {
    if (pos <= 0) return
    var iter = pos
    while (iter < size) {
        calc(iter)
        iter += iter.takeLowestOneBit()
    }
}

inline fun <R> LongArray.bitQueryWithIndex(pos: Int, initial: R, operation: LongArray.(R, Int) -> R): R {
    var iter = pos
    var ans = initial
    while (iter > 0) {
        ans = operation(ans, iter)
        iter -= iter.takeLowestOneBit()
    }
    return ans
}

inline fun <R> LongArray.bitQuery(pos: Int, initial: R, operation: (R, Long) -> R): R {
    var iter = pos
    var ans = initial
    while (iter > 0) {
        ans = operation(ans, this[iter])
        iter -= iter.takeLowestOneBit()
    }
    return ans
}