package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.dq
import com.tauros.cp.graph.Graph
import com.tauros.cp.graph.dfz
import com.tauros.cp.iar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2023/11/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val vtx = iar(n)
    val graph = Graph(n, (n - 1) * 2)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v)
        graph.addEdge(v, u)
    }
    rd.ns(n).withIndex().onEach { (i, c) -> vtx[i] = c - 'a' }

    val singleMasks = (0 until 20).map { 1 shl it }.toIntArray()
    val (ans, mask) = lar(n) to iar(n)
    val all = lar(1 shl 20)
    val qs = ar(n) { dq<Int>() }
    dfz(graph) { root ->
        fun Graph.collect(u: Int, fa: Int, pre: Int, q: ArrayDeque<Int>) {
            val cur = 1 shl vtx[u] xor pre
            mask[u] = cur
            all[cur] += 1L
            q.addLast(u)
            each(u) {
                val v = to[it]
                if (v == fa || deleted[v]) return@each
                collect(v, u, cur, q)
            }
        }

        mask[root] = 1 shl vtx[root]
        ans[root] += 1L
        graph.each(root) {
            val chd = graph.to[it]
            if (deleted[chd]) return@each
            graph.collect(chd, root, 0, qs[chd])
        }

        var throughSum = 0L
        fun Graph.calcAndClear(u: Int, fa: Int): Long {
            val chain = mask[u] xor mask[root]
            var res = if (chain.countOneBits() <= 1) 1L else 0
            var through = all[chain]
            for (single in singleMasks) {
                val find = single xor chain
                through += all[find]
            }
            res += through
            throughSum += through
            each(u) {
                val v = to[it]
                if (v == fa || deleted[v]) return@each
                res += calcAndClear(v, u)
            }
            ans[u] += res
            return res
        }

        graph.each(root) {
            val chd = graph.to[it]
            if (deleted[chd]) return@each
            val q = qs[chd]
            for (i in q) all[mask[i]] -= 1L
            ans[root] += graph.calcAndClear(chd, root)
            for (i in q) all[mask[i]] += 1L
        }
        ans[root] -= throughSum / 2
        mask[root] = 0
        graph.each(root) {
            val chd = graph.to[it]
            if (deleted[chd]) return@each
            val q = qs[chd]
            while (q.isNotEmpty()) {
                val u = q.removeFirst()
                all[mask[u]] -= 1L
                mask[u] = 0
            }
        }
    }

    for (res in ans) wt.print("$res ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}