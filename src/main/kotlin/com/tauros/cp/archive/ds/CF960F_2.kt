package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.avlm
import com.tauros.cp.common.int
import com.tauros.cp.iao

/**
 * @author tauros
 * 2023/12/17
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/960/F
    val (n, m) = rd.ni() to rd.ni()

    val maps = ar(n) { avlm<int, int>(int::compareTo) }
    fun floor(p: int, w: int): int {
        val node = maps[p].floor(w)
        return node?.value ?: 0
    }

    var ans = 0
    repeat(m) {
        val (u, v, w) = iao(rd.ni() - 1, rd.ni() - 1, rd.ni())
        val dp = floor(u, w - 1) + 1
        val old = floor(v, w)
        maps[v][w] = maxOf(old, dp)
        var iter = maps[v].higher(w)
        while (iter != null && iter.value <= dp) {
            maps[v].remove(iter.key)
            iter = maps[v].higher(w)
        }
        ans = maxOf(dp, ans)
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}