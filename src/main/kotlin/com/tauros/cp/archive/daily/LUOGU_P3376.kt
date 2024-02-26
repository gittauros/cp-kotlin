@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.graph.MFLGraph
import com.tauros.cp.graph.mfDinic

/**
 * @author tauros
 */
private val bufCap = 128
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m, s, t) = intArrayOf(rd.ni(), rd.ni(), rd.ni() - 1, rd.ni() - 1)
    val graph = MFLGraph(n, m * 2)
    repeat(m) {
        graph.addEdgeResidual(rd.ni() - 1, rd.ni() - 1, rd.nl())
    }
    val dinic = graph.mfDinic(s, t)
    val ans = dinic.flowAll()
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}