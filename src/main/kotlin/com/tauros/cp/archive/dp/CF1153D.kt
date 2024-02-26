package com.tauros.cp.archive.dp

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph

/**
 * @author tauros
 * 2024/1/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1153/D
    // 神奇的dp，可以通过min/max操作来dp当前节点 第k大排名最小多少 或者 第k小排名最大多少
    // 题解很多求 第k小排名最大多少 的答案，这里写个 第k大排名最小多少 的做法（推一推其实是镜像答案，加深这个dp的理解）
    val n = rd.ni()
    val ops = rd.na(n)

    val graph = Graph(n, n - 1)
    repeat(n - 1) {
        val p = rd.ni() - 1
        graph.addEdge(p, it + 1)
    }

    // min时：其它kth及之后的数都比当前大，所以当前最大排名为 sum{ vKth - 1 } + 1
    // max时：找 min{ vSize - vKth }，size - min{ vSize - vKth }为kth
    data class Info(val size: int, val kth: int)
    fun Graph.dp(u: int, fa: int): Info {
        var size = 0
        val min = ops[u] == 0
        var res = if (min) 0 else n
        each(u) {
            val v = to[it]
            if (v == fa) return@each
            val (vSize, vKth) = dp(v, u)
            size += vSize
            res = if (min) res + vKth - 1 else minOf(res, vSize - vKth)
        }
        return if (size == 0) Info(1, 1)
        else if (min) Info(size, res + 1) else Info(size, size - res)
    }

    val ans = graph.dp(0, -1)
    wt.println(ans.kth)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}