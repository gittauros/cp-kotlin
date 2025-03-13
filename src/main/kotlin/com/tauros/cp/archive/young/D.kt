package com.tauros.cp.archive.young

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2025/3/13
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val cases = rd.ni()
    val mod = 1e9.toInt() + 7
    val mem = mlo<int>(1)
    fun pow(p: int): int {
        while (mem.size <= p) {
            var tmp = mem.last() + mem.last()
            if (tmp >= mod) tmp -= mod
            mem.add(tmp)
        }
        return mem[p]
    }
    repeat(cases) {
        val n = rd.ni()
        val graph = ar(n) { mlo<int>() }
        repeat(n - 1) {
            val (u, v) = rd.na(2).map { it - 1 }
            graph[u].add(v); graph[v].add(u)
        }

        var ans = 0
        fun ansAdd(add: int) {
            ans += add
            if (ans >= mod) ans -= mod
            if (ans < 0) ans += mod
        }
        fun dfs(u: int, fa: int): int {
            val children = mlo<int>()
            for (v in graph[u]) if (v != fa) {
                val chd = dfs(v, u)
                children.add(chd)
            }
            // new
            var chdNew = pow(children.size) - 1 + mod
            if (chdNew >= mod) chdNew -= mod
            ansAdd(chdNew)
            var res = chdNew
            var preSum = 0
            for (chd in children) {
                // cross
                if (children.size >= 2) {
                    val cross = pow(children.size - 2).toLong() * chd % mod * preSum % mod
                    ansAdd(cross.toInt())
                    if (fa != -1) ansAdd(cross.toInt())
                }
                // append
                val append = pow(children.size - 1).toLong() * chd % mod
                ansAdd(append.toInt())
                res += append.toInt()
                if (res >= mod) res -= mod
                preSum += chd
                if (preSum >= mod) preSum -= mod
            }
            return res
        }
        dfs(0, -1)
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}