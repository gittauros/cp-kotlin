package com.tauros.cp.math

import com.tauros.cp.common.MOD


/**
 * @author tauros
 * 2023/9/10
 */
class Matrix(matrix: Array<IntArray>, clone: Boolean = true) : Iterable<IntArray>, Cloneable {
    val rows: Int = matrix.size;
    val cols: Int = matrix[0].size;
    val elements = if (clone) Array(rows) { matrix[it].clone() } else matrix
    constructor(rows: Int, cols: Int = rows, elements: IntArray = IntArray(0)) : this(
        Array(rows) { r -> IntArray(cols) { c -> if (r * cols + c < elements.size) elements[r * cols + c] else 0 } }, false
    )
    companion object {
        fun zero(rows: Int, cols: Int) = Matrix(rows, cols)
        fun one(rows: Int) = Matrix(Array(rows) { r -> IntArray(rows) { c -> if (r == c) 1 else 0 } }, false)
    }
    operator fun get(idx: Int) = elements[idx]
    operator fun plus(other: Matrix) = Matrix(Array(rows) {
        r -> IntArray(cols) { c -> elements[r][c] + other.elements[r][c] }
    }, false)
    operator fun times(other: Matrix): Matrix {
        val ans = Array(rows) { IntArray(other.cols) }
        if (MOD.globalMod > 0) {
            for (i in 0 until rows) for (k in 0 until cols) {
                val thisCell = elements[i][k].toLong()
                if (thisCell == 0L) continue
                for (j in 0 until other.cols) {
                    ans[i][j] += (thisCell * other.elements[k][j] % MOD.globalMod).toInt()
                    if (ans[i][j] >= MOD.globalMod) ans[i][j] -= MOD.globalMod
                }
            }
        } else {
            for (i in 0 until rows) for (k in 0 until cols) {
                if (elements[i][k] == 0) continue
                for (j in 0 until other.cols) {
                    ans[i][j] += elements[i][k] * other.elements[k][j]
                }
            }
        }
        return Matrix(ans, false)
    }
    fun pow(exp: Int) = pow(exp.toLong())
    fun pow(exp: Long): Matrix {
        var (a, p) = this to exp
        var ans: Matrix = one(rows)
        while (p > 0) {
            if (p and 1L == 1L) ans *= a
            a *= a; p = p shr 1
        }
        return ans
    }
    public override fun clone() = Matrix(elements)
    override fun iterator() = object : Iterator<IntArray> {
        private var row = -1
        override fun hasNext() = row + 1 < rows
        override fun next() = elements[++row]
    }
}