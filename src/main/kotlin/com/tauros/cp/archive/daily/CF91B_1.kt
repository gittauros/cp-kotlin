package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/1/23
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/91/B
    val n = rd.ni()
    val nums = rd.na(n)

    val (idx, tmp) = iar(n) { it } to iar(n)
    val ans = iar(n) { -1 }
    fun dac(st: int = 0, ed: int = n) {
        if (st >= ed - 1) return
        val mid = st + ed shr 1
        dac(st, mid); dac(mid, ed)

        var res = -1
        var (iter, p, q) = iao(st, st, mid)
        while (p < mid || q < ed) {
            tmp[iter++] = if (p >= mid || q < ed && nums[idx[q]] < nums[idx[p]]) {
                res = maxOf(res, idx[q])
                idx[q++]
            } else {
                ans[idx[p]] = maxOf(ans[idx[p]], res)
                idx[p++]
            }
        }
        tmp.copyInto(idx, st, st, ed)
    }
    dac()
    for ((i, j) in ans.withIndex()) wt.print("${if (j == -1) -1 else j - i - 1} ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}