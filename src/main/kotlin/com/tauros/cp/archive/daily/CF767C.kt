package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int
import com.tauros.cp.common.swap
import com.tauros.cp.dq
import com.tauros.cp.graph.Graph
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/11/28
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val graph = Graph(n, (n - 1) * 2)
    val vtx = iar(n)
    var root = -2
    for (i in 0 until n) {
        val p = rd.ni() - 1
        if (p != -1) {
            graph.addEdge(p, i)
            graph.addEdge(i, p)
        } else root = i
        vtx[i] = rd.ni()
    }
    val total = vtx.sum()
    if (total % 3 != 0) {
        wt.println(-1)
        return
    }

    val part = total / 3
    data class Frame(val u: int, val fa: int, val out: boolean)
    val stack = dq<Frame>()
    val maps = ar<MutableMap<Int, Int>?>(n) { null }

    val ans = iao(-1, -1)
    stack.addLast(Frame(root, -1, false))
    while (stack.isNotEmpty() && ans[0] == -1) {
        val (u, fa, out) = stack.removeLast()
        if (out) {
            // self
            if (maps[u] == null) maps[u] = mutableMapOf()
            if (u != root && vtx[u] == part * 2 && part in maps[u]!!) {
                ans[0] = u
                ans[1] = maps[u]!![part]!!
            }
            maps[u]!![vtx[u]] = u
            if (fa == -1) continue
            // parent
            vtx[fa] += vtx[u]
            if (maps[fa] == null) {
                maps[fa] = maps[u]
                continue
            }
            if (maps[fa]!!.size < maps[u]!!.size) maps.swap(fa, u)
            if (part in maps[fa]!! && part in maps[u]!!) {
                ans[0] = maps[fa]!![part]!!
                ans[1] = maps[u]!![part]!!
            }
            for ((subSum, sub) in maps[u]!!) maps[fa]!![subSum] = sub
        } else {
            stack.addLast(Frame(u, fa, true))
            graph.each(u) {
                val v = graph.to[it]
                if (v == fa) return@each
                stack.addLast(Frame(v, u, false))
            }
        }
    }

    if (ans[0] == -1) {
        wt.println(-1)
    } else {
        wt.println("${ans[0] + 1} ${ans[1] + 1}")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}