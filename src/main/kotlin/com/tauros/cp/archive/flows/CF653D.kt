package com.tauros.cp.archive.flows

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.boolean
import com.tauros.cp.common.double
import com.tauros.cp.graph.MFIGraph
import com.tauros.cp.graph.mfDinic
import com.tauros.cp.iao
import kotlin.math.abs

/**
 * @author tauros
 * 2023/12/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m, x) = rd.na(3)
    val edges = ar(m) { iao(rd.ni() - 1, rd.ni() - 1, rd.ni()) }

    fun check(per: double): boolean {
        val graph = MFIGraph(n, m * 2)
        for ((u, v, w) in edges) {
            val cnt = (w / per).toInt()
            graph.addEdgeResidual(u, v, cnt)
        }
        val dinic = graph.mfDinic(0, n - 1)
        val res = dinic.flowAll()
        return res >= x
    }

    val eps = 1e-12
    var (l, r) = 0.0 to 1e6
    while (abs(l - r) > eps) {
        val mid = l + (r - l) / 2
        if (mid == l || mid == r) break
        if (check(mid)) l = mid
        else r = mid
    }
    val ans = (l + r) / 2
    wt.printf("%.8f\n", ans * x)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}