package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.heapSort
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.mlo

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
    // https://codeforces.com/contest/1900/problem/D
    val cases = rd.ni()
    val cap = 1e5.toInt()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)
        nums.heapSort()

        val cnt = iar(cap + 1)
        for (num in nums) {
            cnt[num] += 1
        }

        val gt = iar(cap + 1)
        var tot = 0
        for (num in cap downTo  1) {
            gt[num] = tot
            tot += cnt[num]
        }

        val pairs = lar(cap + 1)
        var ans = 0L
        for (num in cap downTo 1) {
            var pre = 0L
            for (mul in num .. cap step num) {
                // 统计三元组
                // 小于当前数 当前数 大于当前数
                pairs[num] += pre * cnt[mul] * gt[mul]
                // 当前数选两个 大于当前数
                pairs[num] += cnt[mul] * (cnt[mul] - 1L) / 2 * gt[mul]
                // 小于当前数 当前数选两个
                pairs[num] += pre * cnt[mul] * (cnt[mul] - 1L) / 2
                // 当前数选三个
                pairs[num] += cnt[mul] * (cnt[mul] - 1L) * (cnt[mul] - 2) / 6

                // 去除多余的
                if (mul > num) pairs[num] -= pairs[mul]
                pre += cnt[mul]
            }
            ans += pairs[num] * num
        }

        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}