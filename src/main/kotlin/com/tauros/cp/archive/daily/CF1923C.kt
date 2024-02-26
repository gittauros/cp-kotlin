package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.long
import com.tauros.cp.runningFold

/**
 * @author tauros
 * 2024/2/23
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, q) = rd.ni() to rd.ni()
        val c = rd.na(n)

        val sum = c.runningFold(0L, long::plus)
        val cnt = c.map { if (it == 1) 1 else 0 }.runningFold(0L, long::plus)
        repeat(q) {
            val (l, r) = rd.ni() to rd.ni()
            if (l == r) {
                wt.println("NO")
            } else {
                val o = cnt[r] - cnt[l - 1]
                val s = sum[r] - sum[l - 1]
                val rest = s - o * 2
                val other = r - l + 1 - o
                val success = rest >= other
                wt.println(if (success) "YES" else "NO")
            }
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}