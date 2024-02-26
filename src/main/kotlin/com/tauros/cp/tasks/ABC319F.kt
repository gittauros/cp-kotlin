package com.tauros.cp.tasks

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.graph.Graph
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.structure.AVLSet
import com.tauros.cp.structure.IntHeap
import java.io.FileInputStream

/**
 * @author tauros
 * 2023/11/20
 */
private val bufCap = 65536

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val graph = Graph(n, (n - 1) * 2)
    val parent = iar(n)
    var cap = 0
    val vtx = ar(n) {
        parent[it] = rd.ni() - 1
        graph.addEdge(it, parent[it])
        graph.addEdge(parent[it], it)
        iao(rd.ni(), rd.ni(), rd.ni()).also { v -> cap = maxOf(cap, v[1]) }
    }

}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}