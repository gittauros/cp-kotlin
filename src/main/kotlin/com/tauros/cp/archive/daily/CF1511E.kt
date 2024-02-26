package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.MInt
import com.tauros.cp.common.int
import com.tauros.cp.common.withMod
import com.tauros.cp.iar
import com.tauros.cp.miar

/**
 * @author tauros
 * 2024/1/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()
    val grid = ar(n) { rd.ns(m) }

    val wCnt = grid.map { line -> line.count { it == 'o' } }.reduce(int::plus)
    val cap = minOf(n * m, 3e5.toInt())
    withMod(998244353) {
        val pow = miar(cap + 1)
        for (i in 0 .. cap) pow[i] = if (i == 0) MInt.ONE else pow[i - 1] * 2
        val res = miar(cap + 1)
        for (i in 0 .. cap) res[i] = if (i < 2) MInt.ONE else res[i - 2] + pow[i - 1]

        fun calc(len: int) = if (len < 2) MInt.ZERO else pow[wCnt - len] * res[len - 2]

        var ans = MInt.ZERO
        val up = ar(2) { iar(m) }
        var cur = 0
        for (i in 0 until n) {
            val pre = cur
            cur = 1 - cur
            up[cur].fill(0)
            var left = 0
            for (j in 0 until m) {
                if (grid[i][j] == 'o') {
                    left += 1
                    up[cur][j] = up[pre][j] + 1
                    ans += calc(left)
                    ans += calc(up[cur][j])
                } else {
                    left = 0
                    up[cur][j] = 0
                }
            }
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}