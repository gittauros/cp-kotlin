package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/19
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, c) = rd.ni() to rd.ni()
    val nums = rd.na(n)
    val cap = 5e5.toInt()
    val pre = iar(cap + 1)
    val cnt = iar(cap + 1)
    var ans = 0
    for (num in nums) {
        if (num != c) {
            pre[num] = minOf(pre[num], cnt[num] - cnt[c])
        }
        cnt[num] += 1
        if (num != c) {
            ans = maxOf(ans, cnt[num] - cnt[c] - pre[num])
        }
    }
    ans += cnt[c]
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}