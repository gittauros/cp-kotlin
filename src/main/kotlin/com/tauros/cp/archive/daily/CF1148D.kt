package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iao

/**
 * @author tauros
 * 2024/4/2
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1148/D
    // 以第一种情况为例，只要第二个数按降序排，就能拼成，那么第二种情况反过来就行，所以排个序就好
    val n = rd.ni()
    val pairs = ar(n) { iao(rd.ni(), rd.ni()) }

    val (lt, gt) = (0 until n).partition {
        val (a, b) = pairs[it]
        a < b
    }

    val ans = if (lt.size >= gt.size) lt.sortedBy { -pairs[it][1] } else gt.sortedBy { pairs[it][1] }
    wt.println(ans.size)
    for (res in ans) wt.print("${res + 1} ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}