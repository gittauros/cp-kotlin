package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/5
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
        val graph = Graph(n, (n - 1) * 2)
        repeat(n - 1) {
            val (u, v) = rd.ni() - 1 to rd.ni() - 1
            graph.addEdge(u, v)
            graph.addEdge(v, u)
        }

        val (vtx, color) = iar(n) to ar(2) { dq<int>() }
        fun Graph.dfs(u: int, fa: int, c: int) {
            color[c].addLast(u)
            each(u) {
                val v = to[it]
                if (v == fa) return@each
                dfs(v, u, 1 - c)
            }
        }
        graph.dfs(0, -1, 0)

        val size = color.map { it.size }
        val (choose, chooseSize) = (if (size[0] < size[1]) 0 else 1) to minOf(size[0], size[1])
        for (i in 1 .. n) {
            val b = i.takeHighestOneBit()
            val u = if (chooseSize and b != 0) choose else 1 - choose
            vtx[color[u].removeFirst()] = i
        }

        for (i in 0 until n) wt.print("${vtx[i]} ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}