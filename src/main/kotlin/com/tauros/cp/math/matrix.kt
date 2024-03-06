package com.tauros.cp.math

import com.tauros.cp.common.MOD
import com.tauros.cp.common.ma
import com.tauros.cp.common.mm


/**
 * @author tauros
 * 2023/9/10
 */
class Matrix(val rows: Int, val cols: Int = rows, elements: IntArray = IntArray(0)) : Iterable<Iterable<Int>>, Cloneable {
    val elements = IntArray(rows * cols) { if (it >= elements.size) 0 else elements[it] }
    constructor(matrix: Array<IntArray>) : this(
        matrix.size, if (matrix.isEmpty()) 0 else matrix[0].size,
        matrix.flatMap { it.toList() }.toIntArray()
    )
    companion object {
        private val ZERO = mutableMapOf<Pair<Int, Int>, Matrix>()
        private val ONE = mutableMapOf<Int, Matrix>()
        fun zero(rows: Int, cols: Int) = ZERO.computeIfAbsent(rows to cols) { (rows, cols) -> Matrix(rows, cols) }
        fun one(rows: Int) = ONE.computeIfAbsent(rows) { cols ->
            Matrix(cols, cols, IntArray(cols * cols) { if (it / cols == it % cols) 1 else 0 })
        }
    }
    class IntArrayView(private val base: Int, private val matrix: Matrix) : Iterable<Int> {
        val size = matrix.cols
        operator fun get(idx: Int) = matrix.elements[base + idx]
        override fun iterator() = object : Iterator<Int> {
            private var idx = 0
            override fun hasNext() = idx < size
            override fun next() = get(idx)
        }
    }
    operator fun get(idx: Int) = IntArrayView(idx * cols, this)
    operator fun plus(other: Matrix): Matrix {
        return Matrix(rows, cols, IntArray(rows * cols) {
            if (MOD.globalMod > 0) {
                elements[it] ma other.elements[it]
            } else {
                elements[it] + other.elements[it]
            }
        })
    }
    operator fun times(other: Matrix): Matrix {
        val ans = IntArray(rows * other.cols)
        if (MOD.globalMod > 0) {
            for (i in 0 until rows) for (j in 0 until other.cols) {
                var sum = 0
                for (k in 0 until cols) {
                    sum = elements[i * cols + k] mm other.elements[k * other.cols + j] ma sum
                }
                ans[i * other.cols + j] = sum
            }
        } else {
            for (i in 0 until rows) for (k in 0 until cols) {
                if (elements[i * cols + k] == 0) continue
                for (j in 0 until other.cols) {
                    ans[i * other.cols + j] += elements[i * cols + k] * other.elements[k * other.cols + j]
                }
            }
        }
        return Matrix(rows, other.cols, ans)
    }
    fun pow(exp: Int) = pow(exp.toLong())
    fun pow(exp: Long): Matrix {
        var (a, p) = this to exp
        var ans: Matrix = one(rows)
        while (p > 0) {
            if (p and 1L == 1L) {
                ans *= a
            }
            a *= a
            p = p shr 1
        }
        return ans
    }
    public override fun clone() = Matrix(rows, cols, elements)
    override fun iterator() = object : Iterator<Iterable<Int>> {
        private var row = 0
        override fun hasNext() = row < rows
        override fun next() = object : Iterable<Int> {
            private val base = row * cols
            override fun iterator() = object : Iterator<Int> {
                private var col = 0
                override fun hasNext() = col < cols
                override fun next() = elements[base + col++]
            }
        }.also { row++ }
    }
    fun toTypedArray() = toList().map { it.toList().toIntArray() }.toTypedArray()
    override fun toString(): String {
        return buildString {
            for (i in 0 until rows) {
                append('[')
                append(buildString {
                    for (j in 0 until cols) {
                        append(elements[i * cols + j])
                        append(", ")
                    }
                }.trimEnd(' ').trimEnd(','))
                append(']')
                append('\n')
            }
        }
    }
}