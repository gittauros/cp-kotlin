@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.graph.IGraph
import com.tauros.cp.structure.IIHeap

/**
 * @author tauros
 */
private val bufCap = 128

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m, l, s, t) = intArrayOf(rd.ni(), rd.ni(), rd.ni(), rd.ni(), rd.ni())
    val graph = IGraph(n, m * 2)
    val modify = BooleanArray(m * 2)
    repeat(m) {
        val (u, v, w) = intArrayOf(rd.ni(), rd.ni(), rd.ni())
        val edgeId = graph.addEdge(u, v, w)
        graph.addEdge(v, u, w)
        if (w == 0) {
            modify[edgeId] = true
            modify[edgeId xor 1] = true
        }
    }
    val INF = 0x3f3f3f3f
    fun IGraph.dij(st: Int, withEdge: IGraph.(Int, Int) -> Unit = { _, _ -> }): IntArray {
        val dist = IntArray(n) { INF }
        val vis = BooleanArray(n)
        val heap = IIHeap { a, b -> a.compareTo(b) }
        dist[st] = 0
        heap.offer(0 to st)
        while (heap.isNotEmpty()) {
            val (curDist, u) = heap.poll()
            if (vis[u]) continue
            vis[u] = true
            each(u) {
                withEdge(it, curDist)
                val (v, w) = to[it] to maxOf(1, weight[it])
                if (curDist + w < dist[v]) {
                    dist[v] = curDist + w
                    heap.offer(dist[v] to v)
                }
            }
        }
        return dist
    }

    val distT = graph.dij(t)
    val distS = graph.dij(s) { edgeId, curDist ->
        if (modify[edgeId] || modify[edgeId xor 1]) {
            val newDist = maxOf(1, l - curDist - distT[to[edgeId]])
            weight[edgeId] = newDist
            weight[edgeId xor 1] = newDist
            modify[edgeId xor 1] = false
            modify[edgeId] = false
        }
    }

    if (distS[t] != l) {
        wt.println("NO")
    } else {
        wt.println("YES")
        graph.each {
            if (it and 1 == 0) {
                val (u, v) = graph.to[it xor 1] to graph.to[it]
                val w = maxOf(1, graph.weight[it])
                wt.println("$u $v $w")
            }
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}