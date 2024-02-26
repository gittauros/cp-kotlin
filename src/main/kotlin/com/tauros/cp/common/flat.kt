package com.tauros.cp.common


/**
 * @author tauros
 * 2023/9/9
 */
class IntFlat(private vararg val sizes: Int, init: (Int) -> Int = { 0 }) {
    private val array = IntArray(sizes.reduceOrNull(Int::times) ?: 0, init)
    private val sufBase = IntArray(sizes.size)

    init {
        for (i in sizes.size - 1 downTo 0) {

        }
    }

    class FlatView<T> {

    }
}