@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily


import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar
import com.tauros.cp.structure.IntHeap
import java.util.TreeSet

/**
 * @author tauros
 * 2023/11/1
 */
private val bufCap = 128

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, m) = rd.ni() to rd.ni()
        val vtx = rd.na(n)
        val graph = Graph(n)
        repeat(m) {
            val (u, v) = rd.ni() - 1 to rd.ni() - 1
            graph.addEdge(u, v)
            graph.addEdge(v, u)
        }
        val set = TreeSet<Int>()
        set.pollFirst()
        val heap = IntHeap(n) { a, b -> vtx[a].compareTo(vtx[b]) }
        val vis = iar(n) { -1 }
        var success = false
        for (i in 0 until n) if (vtx[i] == 0 && vis[i] == -1) {
            heap.clear()
            heap.offer(i)
            vis[i] = i
            var cnt = 0
            while (heap.isNotEmpty()) {
                val u = heap.poll()
                if (vtx[u] > cnt) {
                    break
                }
                cnt++
                graph.each(u) {
                    val v = graph.to[it]
                    if (vis[v] != i) {
                        heap.offer(v)
                        vis[v] = i
                    }
                }
            }
            if (cnt == n) {
                success = true
                break
            }
        }
        wt.println(if (success) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}