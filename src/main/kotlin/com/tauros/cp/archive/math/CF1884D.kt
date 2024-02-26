package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.mlo
import com.tauros.cp.so

/**
 * @author tauros
 * 2023/11/28
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        val cnt = iar(n + 1)
        for (num in nums) cnt[num] += 1

        val pairs = lar(n + 1)
        // 求所有gcd的对数
        var res = 0L
        for (num in n downTo 1) {
            var tot = 0L
            for (mul in num .. n step num) {
                pairs[num] -= pairs[mul]
                tot += cnt[mul]
            }
            // 任选两个
            pairs[num] += tot * (tot - 1L) / 2
            res += pairs[num]
        }

        // 标记所有数组内元素的倍数，答案为非这些倍数的值的gcd对应的对数的和
        val invalid = bar(n + 1)
        var ans = 0L
        for (gcd in 1 .. n) {
            if (cnt[gcd] > 0) {
                for (j in gcd .. n step gcd) invalid[j] = true
            }
            if (!invalid[gcd] && pairs[gcd] > 0) ans += pairs[gcd]
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}