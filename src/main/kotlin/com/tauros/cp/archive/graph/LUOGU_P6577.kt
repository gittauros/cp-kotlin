@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.graph.KMI
import java.io.FileInputStream

/**
 * @author tauros
 */
private val bufCap = 128

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/P6577_11.in"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()
    val graph = Array(n) { IntArray(n) { -INF } }
    repeat(m) {
        val (u, v, w) = intArrayOf(rd.ni() - 1, rd.ni() - 1, rd.ni())
        graph[u][v] = w
    }
    val km = KMI(n, graph, INF)
    km.match()
    var ans = 0L
    val matchStr = buildString {
        for (y in 0 until n) {
            val x = km.yMatch[y]
            ans += graph[x][y]
            append(x + 1)
            append(' ')
        }
    }.trimEnd(' ')
    wt.println(ans)
    wt.println(matchStr)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}