@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 */
private val bufCap = 128
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val n = rd.ni()
    val names = Array<String>(n) {
        rd.ns()
    }
    val graph = Array(26) { mutableSetOf<Int>() }
    val deg = IntArray(26)
    out@ for (i in 1 until n) {
        val pre = names[i - 1]
        val cur = names[i]
        var (p, q) = 0 to 0
        while (p < pre.length && q < cur.length) {
            val (pIdx, qIdx) = pre[p] - 'a' to cur[q] - 'a'
            if (pIdx != qIdx) {
                if (pIdx in graph[qIdx]) {
                    wt.println("Impossible")
                    return
                }
                if (qIdx !in graph[pIdx]) {
                    graph[pIdx].add(qIdx)
                    deg[qIdx]++
                }
                continue@out
            }
            p++
            q++
        }
        if (pre.length > cur.length) {
            wt.println("Impossible")
            return
        }
    }
    val q = ArrayDeque<Int>()
    for (i in 0 until 26) {
        if (deg[i] == 0) {
            q.add(i)
        }
    }
    val ans = mutableListOf<Int>()
    val vis = BooleanArray(26)
    while (q.isNotEmpty()) {
        val u = q.removeFirst()
        ans.add(u)
        vis[u] = true
        for (v in graph[u]) {
            if (--deg[v] == 0) {
                q.add(v)
            }
        }
    }
    if (deg.any { it > 0 }) {
        wt.println("Impossible")
        return
    }
    for (i in 0 until 26) {
        if (!vis[i]) {
            ans.add(i)
        }
    }
    wt.println(buildString {
        ans.map {
            append((it + 'a'.toInt()).toChar())
        }
    })
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}