package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val str = rd.ns().toCharArray()

        val n = str.size
        val lcp = ar(n) { iar(n) }
        for (i in 0 until n) for (j in 0 until n) {
            if (str[i] == '?' || str[j] == '?' || str[i] == str[j]) {
                lcp[i][j] = if (i == 0 || j == 0) 1 else lcp[i - 1][j - 1] + 1
            }
        }

        var ans = 0
        for (ed1 in 0 until n) {
            for (ed2 in ed1 + 1 until n) {
                if (lcp[ed1][ed2] >= ed2 - ed1) {
                    ans = maxOf(ans, (ed2 - ed1) * 2)
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