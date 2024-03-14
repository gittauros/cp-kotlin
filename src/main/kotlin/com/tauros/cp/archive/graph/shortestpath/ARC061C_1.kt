package com.tauros.cp.archive.graph.shortestpath

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.IIP
import com.tauros.cp.dq
import com.tauros.cp.graph.IGraph
import com.tauros.cp.iao

/**
 * @author tauros
 * 2024/3/13
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://atcoder.jp/contests/arc061/tasks/arc061_c
    // 拆点：
    // - 边拆虚点，一个u v c的边创建 u <-1-> uc / uc <-0-> vc / v <-1-> vc 三个边，最终答案要除以2
    // 除以2的原因是进某个公司算了一次，出某个公司的线路也算了一次
    // 这个做法不用重新染色，重边也不需要特殊处理，01BFS就好
    val (n, m) = rd.ni() to rd.ni()
    val edges = ar(m) { id -> rd.na(3).map { it - 1 }.toIntArray() + iao(id) }

    val naming = buildMap {
        var idx = n
        for ((u, v, c) in edges) {
            val (uc, vc) = (u to c) to (v to c)
            if (uc !in this) put(uc, idx++)
            if (vc !in this) put(vc, idx++)
        }
    }
    val graph = IGraph(n + naming.size, 6 * m)
    for ((u, v, c) in edges) {
        val (uc, vc) = (u to c) to (v to c)
        val (ucId, vcId) = naming[uc]!! to naming[vc]!!
        graph.addEdge(ucId, u, 1); graph.addEdge(u, ucId, 1)
        graph.addEdge(vcId, v, 1); graph.addEdge(v, vcId, 1)
        graph.addEdge(ucId, vcId, 0); graph.addEdge(vcId, ucId, 0)
    }

    val inq = bar(n + naming.size)
    val q = dq<IIP>()
    q.addLast(0 to 0); inq[0] = true
    while (q.isNotEmpty()) {
        val (dist, u) = q.removeFirst()
        if (u == n - 1) {
            wt.println(dist / 2); return
        }
        graph.each(u) {
            val (v, w) = graph.to[it] to graph.weight[it]
            if (inq[v]) return@each
            if (w == 0) q.addFirst(dist to v)
            else q.addLast(dist + w to v)
            inq[v] = true
        }
    }
    wt.println(-1)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}