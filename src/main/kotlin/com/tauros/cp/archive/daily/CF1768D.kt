package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.DSU

/**
 * @author tauros
 * 2024/1/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n).map { it - 1 }.toIntArray()

        val dsu = DSU(n)
        for ((i, num) in nums.withIndex()) {
            dsu.merge(i, num, false)
        }

        val vis = bar(n)
        var res = 0
        for (i in 0 until n) if (!vis[dsu.find(i)]) {
            vis[dsu.find(i)] = true
            res += dsu.size(i) - 1
        }

        var ans = INF
        for (i in 0 until n - 1) {
            ans = if (dsu.find(i) == dsu.find(i + 1)) {
                minOf(ans, res - 1)
            } else {
                minOf(ans, res + 1)
            }
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}