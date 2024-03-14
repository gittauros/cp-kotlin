package com.tauros.cp.archive.mex

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/14
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1744/F
    // 枚举mex，那么数组内小于mex的数小于等于数组长度的一半就能统计
    // 求出合法mex区间，然后算左右能扩展最长多少，为lm和rm，求当 0<=l<=lm 且 0<=r<=rm 有多少满足 l+r<=sum 的数量即可
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val perm = rd.na(n)

        val pos = iar(n)
        for ((i, p) in perm.withIndex()) pos[p] = i

        fun calc(lm: int, rm: int, sum: int): long {
            val (a, b) = minOf(lm, rm) to maxOf(lm, rm)
            var res = 0L
            if (a <= sum) {
                res += (a + 1L) * (minOf(b, sum - a) + 1)
                val cnt = minOf(a, b - minOf(b, sum - a))
                if (cnt > 0) {
                    res += (a - cnt + 1L + a) * cnt / 2
                }
            } else {
                val cnt = minOf(sum, b) + 1
                res += (sum + 1L + sum + 1 - cnt + 1) * cnt / 2
            }
            return res
        }

        var ans = 0L
        var (l, r) = pos[0] to pos[0]
        for (p in 1 .. n) if (p == n || pos[p] !in l .. r) {
            val (less, len) = p to r - l + 1
            if (less + less >= len) {
                val rest = less + less - len
                val lMax = if (p == n || pos[p] > r) l else l - pos[p] - 1
                val rMax = if (p == n || pos[p] < l) n - r - 1 else pos[p] - r - 1
                ans += calc(lMax, rMax, rest)
            }
            if (p < n) if (pos[p] < l) l = pos[p] else r = pos[p]
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}