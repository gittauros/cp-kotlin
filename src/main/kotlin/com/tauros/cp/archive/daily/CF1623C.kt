package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.copyInto
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/4/3
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1623/C
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val h = rd.na(n)

        val inf = 0x3f3f3f3f
        val temp = iar(n)
        val ans = findFirst(inf) {
            h.copyInto(temp)
            for (i in n - 1 downTo 0) {
                if (temp[i] < it) return@findFirst true
                if (i >= 2) {
                    val k = minOf((temp[i] - it) / 3, h[i] / 3)
                    temp[i] -= 3 * k
                    temp[i - 1] += k
                    temp[i - 2] += 2 * k
                }
            }
            false
        } - 1
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}