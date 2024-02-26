package com.tauros.cp.archive.pain

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.lar
import com.tauros.cp.structure.LongHeap

/**
 * @author tauros
 * 2023/12/26
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/446/B
    // 对于 (横, 竖, 横) 的操作顺序，显然没有 min((横, 横, 竖), (竖, 横, 横)) 优，因此是横竖是分别考虑更好
    // 因为交叉操作时会比分开操作的顺序上先多扣掉一些数值，所以没有分开操作更优
    // 然后对于分开的操作就可以前后缀分解来贪心了
    val (n, m, k, p) = rd.na(4)
    val mat = ar(n) { rd.na(m) }

    val (rows, cols) = lar(n) to lar(m)
    for (i in 0 until n) for (j in 0 until m) {
        rows[i] += mat[i][j].toLong()
        cols[j] += mat[i][j].toLong()
    }
    val cHeap = LongHeap(cols) { a, b -> -a.compareTo(b) }
    cHeap.build()
    val colSum = lar(k + 1)
    for (i in 1 .. k) {
        val top = cHeap.poll()
        colSum[i] = colSum[i - 1] + top
        cHeap.offer(top - p * n)
    }

    var ans = Long.MIN_VALUE
    val rHeap = LongHeap(rows) { a, b -> -a.compareTo(b) }
    rHeap.build()
    var rowSum = 0L
    var cut = 0L
    for (i in 0 .. k) {
        val rest = k - i
        val res = rowSum + colSum[rest] - rest * cut
        if (res > ans) ans = res
        val top = rHeap.poll()
        rowSum += top
        rHeap.offer(top - p * m)
        cut += p
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}