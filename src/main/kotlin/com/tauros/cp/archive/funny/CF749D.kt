package com.tauros.cp.archive.funny

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2024/4/24
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/749/D
    // https://codeforces.com/problemset/submission/749/257998339
    // 不会求，学了羊解
    val n = rd.ni()
    val highest = iar(n)
    val bids = ar(n) { mlo<int>() }
    repeat(n) {
        val (i, v) = rd.ni() - 1 to rd.ni()
        highest[i] = maxOf(highest[i], v)
        bids[i].add(v)
    }

    val sorted = (0 until n).filter { bids[it].isNotEmpty() }.sortedByDescending { highest[it] }
    val q = rd.ni()
    val removed = bar(n)
    repeat(q) {
        val k = rd.ni()
        val l = rd.na(k).map { it - 1 }
        for (i in l) removed[i] = true
        val heads = buildList {
            for (i in sorted) {
                if (size >= 2) break
                if (removed[i]) continue
                add(i)
            }
        }
        if (heads.isEmpty()) wt.println("0 0")
        else if (heads.size == 1) wt.println("${heads[0] + 1} ${bids[heads[0]][0]}")
        else {
            val (first, second) = heads
            val cap = highest[second]
            val idx = findFirst(bids[first].size) { bids[first][it] > cap }
            wt.println("${first + 1} ${bids[first][idx]}")
        }
        for (i in l) removed[i] = false
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}