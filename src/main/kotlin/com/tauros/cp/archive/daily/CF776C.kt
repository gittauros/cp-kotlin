package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.lar
import com.tauros.cp.mmo
import com.tauros.cp.runningFold

/**
 * @author tauros
 * 2023/11/21
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val CAP = 1e14.toLong()

private fun solve() {
    val (n, k) = rd.ni() to rd.ni()
    val nums = rd.nal(n)
    val sum = nums.runningFold(0L, Long::plus)
    val pow = buildSet {
        var cur = 1L
        while (cur >= -CAP && cur <= CAP) {
            if (cur in this) break
            add(cur)
            cur *= k
        }
    }.toLongArray()
    val cnt = mmo<Long, Int>()
    cnt[0L] = 1
    var ans = 0L
    for (i in 1 .. n) {
        for (p in pow) {
            ans += cnt.getOrDefault(sum[i] - p, 0)
        }
        cnt[sum[i]] = cnt.getOrDefault(sum[i], 0) + 1
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}