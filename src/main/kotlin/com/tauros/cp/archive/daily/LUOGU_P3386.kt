@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.graph.MFIGraph
import com.tauros.cp.graph.mfDinic

/**
 * @author tauros
 */
private val bufCap = 128

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/P3386_8.in"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m, e) = intArrayOf(rd.ni(), rd.ni(), rd.ni())
    val graph = MFIGraph(n + m + 2)
    val (S, T) = n + m to n + m + 1
    for (i in 0 until n) {
        graph.addEdgeResidual(S, i, 1)
    }
    for (i in 0 until m) {
        graph.addEdgeResidual(i + n, T, 1)
    }
    repeat(e) {
        val (x, y) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdgeResidual(x, y + n, 1)
    }
    val mf = graph.mfDinic(S, T)
    val ans = mf.flowAll()
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}