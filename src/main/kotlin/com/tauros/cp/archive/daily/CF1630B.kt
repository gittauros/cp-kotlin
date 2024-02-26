package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.IIP
import com.tauros.cp.common.boolean
import com.tauros.cp.common.findFirst
import com.tauros.cp.dq
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/1/4
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1630/B
    val cases = rd.ni()
    repeat(cases) {
        val (n, k) = rd.ni() to rd.ni()
        val nums = rd.na(n)

        val cnt = iar(n + 1)
        for (num in nums) cnt[num]++

        var (cl, cr) = 1 to n + 1
        var (diff, l, r) = iao(-n, 1, 1)
        while (l <= n) {
            while (r <= n && diff < k) diff += 2 * cnt[r++]
            if (diff >= k && r - l < cr - cl) {
                cl = l
                cr = r
            }
            diff -= 2 * cnt[l++]
        }

        var pre = 0; diff = 0
        val ans = dq<IIP>()
        for ((i, num) in nums.withIndex()) {
            diff += if (num in cl until cr) 1 else -1
            if (diff > 0) {
                ans.addLast(pre to i)
                pre = i + 1
                diff = 0
            }
        }
        while (ans.isNotEmpty() && diff <= 0) {
            l = ans.removeLast().first
            diff++
        }
        if (diff > 0) ans.addLast(l to n - 1)
        while (ans.size > k) {
            r = ans.removeLast().second
            l = ans.removeLast().first
            ans.addLast(l to r)
        }

        wt.println("$cl ${cr - 1}")
        for ((st, ed) in ans) {
            wt.println("${st + 1} ${ed + 1}")
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}