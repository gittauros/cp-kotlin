package com.tauros.cp.archive.young

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2025/3/11
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, a, b, c, l) = rd.na(5)

        val tips = rd.na(n + 1)
        val set = tips.toSet()
        fun part1(x: int): int {
            for (tip in tips) {
                if (tip - x in 0..l && tip - x in set) return 0
            }
            return 1
        }

        fun part2(x: int, y: int): int {
            val (p, q) = minOf(x, y) to maxOf(x, y)
            var ans = 2
            for (tip in tips) {
                if (tip - (q - p) in set && (tip + p <= l || tip - q >= 0)) ans = minOf(ans, 1)
                if (tip - (p + q) in set) ans = minOf(ans, 1)
                if (tip - p in set && tip - q in set) return 0
                if ((tip - p in set || tip - q in set) && tip - (p + q) in set) return 0
            }
            return ans
        }

        var ans = part1(a) + part1(b) + part1(c)
        ans = minOf(ans, part1(a) + part2(b, c))
        ans = minOf(ans, part1(b) + part2(a, c))
        ans = minOf(ans, part1(c) + part2(a, b))

        val x = iao(a, b, c)
        val ends = iar(4)
        val vis = bar(3)
        fun dfs(dep: int, res: int): int {
            if (dep == 3) return res
            var min = 3
            for (ed in (0..dep).map { ends[it] }) for (i in 0 until 3) if (!vis[i]) {
                vis[i] = true
                if (ed - x[i] >= 0) {
                    val nex = ed - x[i]
                    ends[dep + 1] = nex
                    val chd = dfs(dep + 1, res + (if (nex in set) 0 else 1))
                    min = minOf(min, chd)
                }
                if (ed + x[i] <= l) {
                    val nex = ed + x[i]
                    ends[dep + 1] = nex
                    val chd = dfs(dep + 1, res + (if (nex in set) 0 else 1))
                    min = minOf(min, chd)
                }
                vis[i] = false
            }
            return min
        }
        for (tip in tips) {
            ends[0] = tip
            val res = dfs(0, 0)
            ans = minOf(res, ans)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}