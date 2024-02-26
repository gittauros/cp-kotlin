package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.findFirst
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.structure.IIHeap

/**
 * @author tauros
 * 2023/11/25
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, m) = rd.na(2)
        val flowers = ar(m) { rd.na(2) }
        flowers.sortBy { -it[0] }
        val sums = lar(m + 1)
        for (i in 1 .. m) sums[i] = sums[i - 1] + flowers[i - 1][0]
        var ans = 0L
        for (i in 0 until m) {
            val st = findFirst(m) { flowers[it][0] <= flowers[i][1] } - 1
            val cnt = minOf(st + 1, n)
            var rest = n - cnt
            var res = if (i >= cnt) {
                if (rest == 0) sums[cnt - 1] + flowers[i][0]
                else {
                    rest -= 1
                    sums[cnt] + flowers[i][0]
                }
            } else sums[cnt]
            res += rest.toLong() * flowers[i][1]
            ans = maxOf(ans, res)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}