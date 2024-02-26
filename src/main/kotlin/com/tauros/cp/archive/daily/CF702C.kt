package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst

/**
 * @author tauros
 * 2023/12/19
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()
    val cities = rd.na(n).sortedArray()
    val towers = rd.na(m).sortedArray()
    var ans = 0
    for (city in cities) {
        val right = findFirst(m) { towers[it] >= city }
        val r = if (right == m) Int.MAX_VALUE else towers[right] - city
        val left = findFirst(m) { towers[it] > city } - 1
        val l = if (left == -1) Int.MAX_VALUE else city - towers[left]
        ans = maxOf(ans, minOf(l, r))
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}