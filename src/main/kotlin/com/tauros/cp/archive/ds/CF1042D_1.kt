package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.runningFold
import com.tauros.cp.structure.bitQuery
import com.tauros.cp.structure.bitUpdateWithIndex

/**
 * @author tauros
 * 2023/12/30
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1042/D
    val (n, t) = rd.ni() to rd.nl()
    val nums = rd.na(n)
    val sums = nums.runningFold(0L, Long::plus)
    // sums[r] - sums[l] < t
    // sums[l] > sums[r] - t
    val sorted = sums.sorted().distinct().toLongArray()

    fun IntArray.update(pos: int) = this.bitUpdateWithIndex(pos) { this[it] += 1 }
    fun IntArray.query(pos: int) = this.bitQuery(pos, 0, Int::plus)

    val bit = IntArray(sorted.size + 2)
    var ans = 0L
    bit.update(findFirst(sorted.size) { sorted[it] >= 0 } + 1)
    for (i in 1 until sums.size) {
        val sum = sums[i]
        val gt = sum - t

        val idx = findFirst(sorted.size) { sorted[it] > gt } - 1
        val res = i - bit.query(idx + 1)
        ans += res

        bit.update(findFirst(sorted.size) { sorted[it] >= sum } + 1)
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}