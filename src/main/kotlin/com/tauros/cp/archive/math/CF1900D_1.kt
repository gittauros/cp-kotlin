package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
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
    val divs = ar(cap + 1) { mlo<int>() }
    for (i in 1 .. cap) {
        for (j in i .. cap step i) {
            divs[j].add(i)
        }
    }
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n).sortedArray()

        val cnt = iar(cap + 1)
        val pairs = lar(cap + 1)
        // f(a, b, c) 枚举b，计算前缀位置与b的约数形成的对数
        // 也即是b的约数作为一个pair的gcd的次数
        // 设该约数是d，这样会把 2*d 3*d ... 多计算进去
        // 也即是d作为gcd的对数为 d对数 - (2*d对数 + 3*d对数 ...)
        for ((i, num) in nums.withIndex()) {
            for (div in divs[num]) {
                pairs[div] += cnt[div] * (n - 1L - i)
                cnt[div] += 1
            }
        }

        var ans = 0L
        // 从高到低来处理
        for (num in nums.last() downTo 1) {
            // 枚举num的倍数将其2倍以上的数值去除，达到了把多余计算部分去除的效果
            // 不会多减，因为num的倍数如果不在范围内那么不会在前面的计算中被统计
            // 相当于是个dp的过程
            for (mul in num + num .. cap step num) {
                // 不会出现负数，num的倍数如果有值，由于num一定是num倍数的约数，肯定也会在前面被加上值
                pairs[num] -= pairs[mul]
            }
            if (pairs[num] != 0L) {
                ans += pairs[num] * num
            }
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}