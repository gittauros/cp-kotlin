package com.tauros.cp.archive.pain

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.swap
import com.tauros.cp.iar
import com.tauros.cp.structure.IntHeap

/**
 * @author tauros
 * 2024/2/7
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1822/E
    val cases = rd.ni()
    val cap = 26
    repeat(cases) {
        val n = rd.ni()
        val str = rd.ns(n)

        val cnt = iar(cap)
        for (c in str) {
            cnt[c - 'a'] += 1
        }
        val most = (0 until 26).maxBy { cnt[it] }
        if (n % 2 == 1 || cnt[most] > (n + 1) / 2) {
            wt.println(-1)
            return@repeat
        }

        val same = iar(cap)
        var (p, q) = 0 to n - 1
        while (p < q) {
            if (str[p] == str[q]) same[str[p] - 'a'] += 1
            p += 1; q -= 1
        }

        val (sum, max) = same.sum() to same.max()
        if (max + max > sum) wt.println(max)
        else wt.println((sum + 1) / 2)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}