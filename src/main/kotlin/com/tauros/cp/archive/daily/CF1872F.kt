package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.iar
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2024/1/28
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
        val afr = rd.na(n).map { it - 1 }.toIntArray()
        val cost = rd.na(n)

        val deg = iar(n)
        afr.forEach { deg[it] += 1 }

        val ans = mlo<int>()
        val q = dq<int>()
        for ((i, d) in deg.withIndex()) if (d == 0) q.addLast(i)
        val vis = bar(n)
        while (q.isNotEmpty()) {
            val u = q.removeFirst()
            vis[u] = true
            ans.add(u)
            if (--deg[afr[u]] == 0) q.addLast(afr[u])
        }

        for (i in 0 until n) if (!vis[i]) {
            var iter = i
            var st = i
            do {
                vis[iter] = true
                if (cost[iter] < cost[st]) st = iter
                iter = afr[iter]
            } while (iter != i)
            iter = afr[st]
            do {
                ans.add(iter)
                iter = afr[iter]
            } while (iter != afr[st])
        }

        for (res in ans) wt.print("${res + 1} ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}