package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.avlm

/**
 * @author tauros
 * 2023/11/21
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
        val ranges = ar(n) { rd.na(4) }
        ranges.sortBy { -it[3] }
        val q = rd.ni()
        val queries = rd.na(q)

        var max = -1
        var iter = 1e9.toInt() + 1
        val map = avlm<Int, Int> { a, b -> a.compareTo(b) }
        for ((l, _, _, b) in ranges) {
            if (b >= max && l >= iter) continue
            if (b < iter) {
                if (iter <= max) map[iter] = max
                max = b
            }
            iter = minOf(iter, l)
        }
        if (iter <= max) map[iter] = max

        for (query in queries) {
            val r = map.floor(query)?.value ?: -1
            val ans = maxOf(query, r)
            wt.print(ans)
            wt.print(" ")
        }
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}