package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/11/22
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val m = rd.ni()
        val grid = ar(2) { rd.na(m) }
        val suf = ar(2) { iar(m + 1) }
        for (i in m - 1 downTo 1) {
            val len = (m - i) * 2
            for (j in 0 until 2) {
                suf[j][i] = maxOf(grid[1 - j][i] + 1, grid[j][i] + len, suf[j][i + 1] + 1)
            }
        }
        var pre = grid[1][0] + 1
        var ans = minOf(
            maxOf(grid[1][0] + 1, grid[0][1] + 1 + (m - 1) * 2, suf[0][1] + 1),
            maxOf(grid[1][0] + 1, pre + (m - 1) * 2, suf[1][1])
        )
        for (i in 2 until m) {
            pre = maxOf(pre + 1, grid[1 - i % 2][i - 1] + 1)
            pre = maxOf(pre + 1, grid[i % 2][i - 1] + 1)
            val res = maxOf(grid[1 - i % 2][i] + 1, pre + (m - i) * 2, suf[i % 2][i])
            ans = minOf(ans, res)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}