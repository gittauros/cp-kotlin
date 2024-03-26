package com.tauros.cp.archive.bipartite

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2024/3/13
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1702/E
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val dominoes = ar(n) { rd.na(2) }

        if (dominoes.any { it[0] == it[1] }) {
            wt.println("NO")
            return@repeat
        }
        val idMap = buildMap<int, MutableList<int>> {
            for (i in 0 until n) {
                val (u, v) = dominoes[i]
                computeIfAbsent(u) { mlo<int>() }.add(i)
                computeIfAbsent(v) { mlo<int>() }.add(i)
            }
        }

        val color = iar(n)
        fun dfs(u: int, c: int): boolean {
            if (color[u] == c) return true
            if (color[u] == 3 - c) return false
            color[u] = c
            for (num in dominoes[u]) for (v in idMap[num]!!) if (v != u) {
                if (!dfs(v, 3 - c)) return false
            }
            return true
        }
        for (i in 0 until n) if (color[i] == 0) {
            if (!dfs(i, 1)) {
                wt.println("NO")
                return@repeat
            }
        }
        wt.println("YES")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}