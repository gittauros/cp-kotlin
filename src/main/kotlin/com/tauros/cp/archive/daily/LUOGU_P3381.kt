@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.graph.MCMFIGraph
import com.tauros.cp.graph.mcmfPrimalDual
import java.io.FileInputStream
import kotlin.system.measureTimeMillis

/**
 * @author tauros
 */
private val bufCap = 128

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/P3381_8.in"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m, s, t) = intArrayOf(rd.ni(), rd.ni(), rd.ni() - 1, rd.ni() - 1)
    val graph = MCMFIGraph(n, m * 2)
    repeat(m) {
        val (u, v) = intArrayOf(rd.ni() - 1, rd.ni() - 1)
        val (w, c) = rd.na(2)
        graph.addEdgeResidual(u, v, w, c)
    }
    val mcmf = graph.mcmfPrimalDual(s, t)
    val (flow, cost) = mcmf.flowAll()
    wt.println("$flow $cost")
}

fun main(args: Array<String>) {
//    measureTimeMillis {
        solve()
//    }.also {
//        wt.println("$it ms")
//    }
    wt.flush()
}