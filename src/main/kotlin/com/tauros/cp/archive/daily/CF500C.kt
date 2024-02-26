package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.structure.bitQuery
import com.tauros.cp.structure.bitUpdateWithIndex

/**
 * @author tauros
 * 2024/1/31
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()
    val w = rd.na(n)
    val read = rd.na(m).map { it - 1 }.toIntArray()

    fun IntArray.update(pos: int, add: int) = this.bitUpdateWithIndex(pos) { this[it] += add }
    fun IntArray.query(pos: int) = this.bitQuery(pos, 0, int::plus)

    val bit = iar(m + 1)
    val pre = iar(n)
    var ans = 0
    for (i in 1 .. m) {
        val j = read[i - 1]
        if (pre[j] > 0) bit.update(pre[j], -w[j])

        ans += bit.query(i) - bit.query(pre[j])
        bit.update(i, w[j])
        pre[j] = i
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}