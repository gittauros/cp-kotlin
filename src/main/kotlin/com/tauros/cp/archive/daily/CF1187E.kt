package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/21
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1187/E
    // 手玩一下，发现选中一个根节点后怎么走都不影响答案，答案只有根节点确定，所以可以换根dp
    // 再手玩一下，确定一下换根的时候多了哪个子树，少了哪个子树，就做完了
    val n = rd.ni()
    val graph = Graph(n, (n - 1) * 2)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
    }

    val size = iar(n)
    var ans = run {
        fun Graph.dfs(u: int, fa: int): long {
            size[u] = 1
            var res = 0L
            each(u) {
                val v = to[it]
                if (v == fa) return@each
                res += dfs(v, u)
                size[u] += size[v]
            }
            res += size[u]
            return res
        }
        graph.dfs(0, -1)
    }

    fun Graph.dfs(u: int, fa: int, cur: long) {
        ans = maxOf(ans, cur)
        each(u) {
            val v = to[it]
            if (v == fa) return@each
            val next = cur - size[v] + n - size[v]
            dfs(v, u, next)
        }
    }
    graph.dfs(0, -1, ans)
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}