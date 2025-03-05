package com.tauros.cp.archive.young

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar

/**
 * @author tauros
 * 2025/3/2
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val cases = rd.ni()
    repeat(cases) { case ->
        val n = rd.ni()
        val graph = mutableMapOf<int, MutableList<int>>()
        repeat(n - 1) {
            val (u, v) = rd.ni() - 1 to rd.ni() - 1
            graph.computeIfAbsent(u) { mutableListOf() }.add(v)
            graph.computeIfAbsent(v) { mutableListOf() }.add(u)
        }

        fun dfs1(u: int, fa: int = -1): IIP {
            var (dis, far) = 0 to u
            for (v in graph[u]!!) {
                if (v == fa) continue
                val (vDis, vFar) = dfs1(v, u)
                if (vDis + 1 > dis) {
                    dis = vDis + 1
                    far = vFar
                }
            }
            return dis to far
        }

        val root = dfs1(0).second

        val onDiameter = bar(n)
        val next = iar(n) { -1 }
        fun dfs2(u: int, fa: int = -1): int {
            var dis = 0
            for (v in graph[u]!!) {
                if (v == fa) continue
                val vDis = dfs2(v, u)
                if (vDis + 1 > dis) {
                    dis = vDis + 1
                    next[u] = v
                }
            }
            return dis
        }
        dfs2(root)
        var iter = root
        while (iter != -1) {
            onDiameter[iter] = true
            iter = next[iter]
        }

        fun dfs3(u: int, fa: int = -1): int {
            var dis = 0
            for (v in graph[u]!!) {
                if (v == fa || onDiameter[v]) continue
                val vDis = dfs3(v, u)
                dis = maxOf(vDis + 1, dis)
            }
            return dis
        }
        iter = root
        var success = true
        while (iter != -1) {
            val dis = dfs3(iter)
            if (dis > 1) {
                success = false
                break
            }
            iter = next[iter]
        }
        wt.println(if (success) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}