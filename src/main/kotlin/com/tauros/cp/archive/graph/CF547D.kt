package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.car
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/30
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/547/D
    // 二分图染色：
    // 双色染色问题，先试试二分图。
    //
    // 建图：
    // 同一水平线上的点，两个两个一对，连一条边。如果有奇数个点，多出的一个点不连边。
    // 同一垂直线上的点，两个两个一对，连一条边。如果有奇数个点，多出的一个点不连边。
    //
    // 由于图中任意路径一定是 水平边-垂直边-水平边-垂直边 交替进行，所以的得到的图一定是二分图（可能有多个连通块）。
    // 直接二分图染色，即可得到答案。
    //
    // 欧拉回路：
    // 我们转化一下问题: 将点看成连接其横坐标和纵坐标的边, 我们为每一条边定向, 使得每一个点的入度与出度的差的绝对值不超过1
    // 这个很容易可以转化成一个欧拉回路的问题: 显然度数为偶数的点入度等于出度, 而度数为奇数的点入出度恰好差1
    // 我们将度数为奇数的点连一条虚拟边到一个虚拟点, 由于的度数为奇数的点必然是偶数个, 那么虚拟点的度数也为偶数, 我们对新图跑欧拉回路对边定向即可.
    val n = rd.ni()
    val base = 2e5.toInt()
    val nodeCap = base + base + 1
    val graph = Graph(nodeCap)
    val deg = iar(nodeCap)

    repeat(n) {
        val (r, c) = rd.ni() to rd.ni() + base
        graph.addEdge(r, c)
        graph.addEdge(c, r)
        // 统计度数
        deg[r]++
        deg[c]++
    }

    // 往虚拟点添加边
    for (i in 1 .. base + base) if (deg[i] % 2 == 1) {
        graph.addEdge(i, 0)
        graph.addEdge(0, i)
    }

    val vis = bar(graph.totalEdgeCnt / 2)
    val ans = car(n)
    // 欧拉回路
    fun Graph.dfs(u: int) {
        var iter = first[u]
        while (iter != -1) {
            // 走一条边删一条边，避免每个点要重复判断是否访问了
            first[u] = next[iter]
            // n条双向边，每条边对应的原始id刚好就是一半
            val id = iter / 2
            if (!vis[id]) {
                vis[id] = true
                val v = to[iter]
                // 前n条双向边可以统计答案
                // 判断u为x轴的点，方向为x轴指向y轴就标r，否则b
                if (id < n) ans[id] = if (u <= base) 'r' else 'b'
                dfs(v)
            }
            iter = first[u]
        }
    }
    // 跑欧拉回路
    for (r in 1 .. base) graph.dfs(r)

    for (c in ans) wt.print(c)
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}