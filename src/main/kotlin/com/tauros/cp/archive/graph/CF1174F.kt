package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/6
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1174/F
    val n = rd.ni()
    val graph = Graph(n, (n - 1) * 2)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
    }

    var cnt = 0
    fun askDist(u: int): int {
        wt.println("d ${u + 1}")
        wt.flush()
        cnt += 1
        return rd.ni()
    }
    fun askNext(u: int): int {
        wt.println("s ${u + 1}")
        wt.flush()
        cnt += 1
        return rd.ni() - 1
    }

    val (parent, dep) = iar(n) to iar(n)
    fun Graph.dfs(u: int, fa: int, d: int = 0) {
        dep[u] = d
        parent[u] = fa
        each(u) {
            val v = to[it]
            if (v == fa) return@each
            dfs(v, u, d + 1)
        }
    }
    graph.dfs(0, -1)


    fun Graph.nextQuery(root: int, len: int): int {
        // 筛出仅在len距离内的子树，询问其重心
        // 重心最大子树最多为一半节点数，可保证下次询问比前一次减少一半点数
        val size = iar(n)
        fun size(u: int, d: int = 0): int {
            size[u] = 1
            if (d == len) return 1
            each(u) {
                val v = to[it]
                if (v == parent[u] || v == parent[root]) return@each
                size[u] += size(v, d + 1)
            }
            return size[u]
        }
        val total = size(root)
        fun centroid(u: int): Int {
            var maxSon = 0
            each(u) {
                val v = to[it]
                if (v == parent[u] || v == parent[root]) return@each
                val res = centroid(v)
                if (res != -1) return res
                maxSon = maxOf(maxSon, size[v])
            }
            val upSon = total - size[u]
            maxSon = maxOf(maxSon, upSon)

            return if (maxSon + maxSon <= total) u else -1
        }
        return centroid(root)
    }
    tailrec fun Graph.goUp(u: int, len: int): int = if (len == 0) u else goUp(parent[u], len - 1)

    //              ○ ←cur
    //     边长a →  /
    //            /
    //           ○ ←跳到这里然后再往x走一格
    //  边长b →  /\
    //         /  \  ← 边长c
    //     x→ ○    ○ ←u
    // 找出子树重心u进行询问，有：
    // a + b = curDist
    // b + c = queryDist
    // a + c = dep[query] - dep[cur]
    // 则 c = (queryDist + (dep[query] - dep[cur]) - curDist) / 2
    // u 网上跳 c 后再往 x 走一格
    // 因为u为重心，每次要么进入一半点数的子树，要么丢弃一半点数的子树，总共做到logn次循环
    var (cur, curDist) = 0 to askDist(0)
    while (curDist > 0) {
        val query = graph.nextQuery(cur, curDist)
        val queryDist = askDist(query)
        val diff = (queryDist + (dep[query] - dep[cur]) - curDist) / 2
        if (diff == 0) {
            cur = query
            curDist = queryDist
        } else {
            cur = graph.goUp(query, diff)
            curDist = queryDist - diff
        }
        if (curDist != 0) {
            cur = askNext(cur)
            curDist -= 1
        }
    }
    wt.println("! ${cur + 1}")
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}