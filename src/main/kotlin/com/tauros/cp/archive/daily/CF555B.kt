package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.DSU
import com.tauros.cp.common.findFirst
import com.tauros.cp.iar
import com.tauros.cp.lao
import com.tauros.cp.lar

/**
 * @author tauros
 * 2023/12/17
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()
    val islands = ar(n) { rd.nal(2) }
    val bridges = lar(m) { rd.nl() }

    val idxes = iar(m) { it }.sortedBy { bridges[it] }.toIntArray()
    val ranges = ar(n - 1) {
        lao(islands[it + 1][0] - islands[it][1], islands[it + 1][1] - islands[it][0], it.toLong())
    }.sortedBy { it[1] }

    val dsu = DSU(m + 1)
    val ans = iar(n - 1)
    for ((cl, cr, i) in ranges) {
        val idx = findFirst(m) { bridges[idxes[it]] >= cl }
        val able = dsu.find(idx)
        if (able == m || bridges[idxes[able]] > cr) {
            wt.println("No")
            return
        }
        ans[i.toInt()] = idxes[able] + 1
        dsu.merge(able, able + 1)
    }

    wt.println("Yes")
    for (i in 0 until n - 1) wt.print("${ans[i]} ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}