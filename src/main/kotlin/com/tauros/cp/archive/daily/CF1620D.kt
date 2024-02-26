package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/11/30
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
        val nums = rd.na(n)
        var ans = INF
        for (reserve1 in 0 .. 2) for (reserve2 in 0 .. 2) {
            var cnt3 = 0
            for (num in nums) {
                var cur3 = INF
                for (use1 in 0 .. reserve1) for (use2 in 0 .. reserve2) {
                    val rest = num - use1 - use2 * 2
                    if (rest >= 0 && rest % 3 == 0) {
                        cur3 = minOf(cur3, rest / 3)
                    }
                }
                cnt3 = maxOf(cnt3, cur3)
            }
            ans = minOf(ans, cnt3 + reserve1 + reserve2)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}