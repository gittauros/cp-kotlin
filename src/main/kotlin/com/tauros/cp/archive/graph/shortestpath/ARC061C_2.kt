package com.tauros.cp.archive.graph.shortestpath

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.graph.IGraph
import com.tauros.cp.iao
import com.tauros.cp.iar

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
    // - 二分图拆法，左部为站点，右部为公司，一个u v c的边创建 左部u到右部c 和 左部v到右部c 两个边权为1的边，最终答案要除以2
    // 除以2的原因是进某个公司算了一次，出某个公司的线路也算了一次
    // 二分图的比较好命名，边数也少些，但是不是一个连通块的相同颜色需要被处理成两种颜色，也就是要重新染色
    // 重新染色后就和CF1941G里一样，保证了同一个颜色是同一个连通块
    // 注意重边，可能会有u v c都相同的边
    // 如果c不同，下面的处理方式可以正确处理；如果c也相同，那么有些边要忽略掉
    // 边权都是1，不用记录，直接BFS
    val (n, m) = rd.ni() to rd.ni()
    val edges = ar(m) { id -> rd.na(3).map { it - 1 }.toIntArray() + iao(id) }

    val available = bar(m)
    val colors = run {
        val colorMap = edges.groupBy { it[2] }
        val graph = IGraph(n, m * 2, removeAble = true)
        val (q, vis) = iar(m * 2) to bar(n)
        var idx = 0
        for ((_, cur) in colorMap) {
            var (head, tail) = 0 to 0
            for ((u, v, _, edgeId) in cur) {
                q[tail++] = u; q[tail++] = v
                graph.addEdge(u, v, edgeId)
                graph.addEdge(v, u, edgeId)
            }
            fun IGraph.dfs(u: int, fa: int, c: int) {
                vis[u] = true
                each(u) {
                    val (v, id) = to[it] to weight[it]
                    if (v == fa || vis[v]) return@each
                    edges[id][2] = c; available[id] = true
                    dfs(v, u, c)
                }
            }
            for (i in 0 until tail) {
                val u = q[i]; if (vis[u]) continue
                graph.dfs(u, -1, idx++)
            }
            while (head < tail) {
                val u = q[head++]
                vis[u] = false; graph.clear(u)
            }
        }
        idx
    }

    val graph = Graph(n + colors)
    for ((u, v, c, id) in edges) if (available[id]) {
        val vtxC = c + n
        graph.addEdge(u, vtxC); graph.addEdge(vtxC, u)
        graph.addEdge(v, vtxC); graph.addEdge(vtxC, v)
    }
    val (q, inq) = iar(n + colors) to bar(n + colors)
    var (head, tail) = 0 to 0
    var step = 0; q[tail++] = 0; inq[0] = true
    while (head < tail) {
        repeat(tail - head) {
            val u = q[head++]
            if (u == n - 1) {
                wt.println(step / 2); return
            }
            graph.each(u) {
                val v = graph.to[it]
                if (inq[v]) return@each
                q[tail++] = v; inq[v] = true
            }
        }
        step += 1
    }
    wt.println(-1)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}