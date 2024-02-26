package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2023/11/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val lens = rd.na(n).sortedArrayDescending()
    var ans = 0L
    var i = 0
    while (i < lens.size) {
        while (i + 1 < lens.size && lens[i + 1] < lens[i] - 1) i++
        if (i + 1 >= lens.size) break
        val w = lens[i + 1].also { i += 2 }
        if (i >= lens.size) break
        while (i + 1 < lens.size && lens[i + 1] < lens[i] - 1) i++
        if (i + 1 >= lens.size) break
        val l = lens[i + 1].also { i += 2 }
        ans += w.toLong() * l
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}