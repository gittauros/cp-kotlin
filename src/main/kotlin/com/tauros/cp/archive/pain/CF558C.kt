package com.tauros.cp.archive.pain

import com.tauros.cp.iar
import kotlin.math.abs

/**
 * @author tauros
 * 2024/4/16
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/558/C
    // 想贪心拿最长相同的前缀，然后搞个中位数贪心，WA了
    // 看了羊解发现求最长相同前缀的逻辑错掉了，好难啊
    val n = readln().toInt()
    val nums = readln().split(" ").map { it.toInt() }.toIntArray()

    val cap = 1e5.toInt()
    val cnt = iar(cap + 1)
    for (num in nums) {
        var iter = num
        while (iter > 0) {
            cnt[iter] += 1
            iter = iter shr 1
        }
    }
    val mask = (cap downTo 1).first { cnt[it] == n }

    val mh = mask.takeHighestOneBit()
    var ans = 0
    val tails = buildList {
        for (i in 0 until n) {
            val num = nums[i]
            val h = num.takeHighestOneBit()
            val times = (h / mh) * mask
            val xor = times xor num
            if (xor == 0) {
                add(times.countTrailingZeroBits())
            } else {
                val bits = xor.takeHighestOneBit().countTrailingZeroBits() + 1
                ans += bits
                add(((num shr bits) / mask).countTrailingZeroBits())
            }
        }
    }.sorted().toIntArray()

    val mid = tails[(n - 1) / 2]
    val res = (0 until n).sumOf { abs(tails[it] - mid) }
    ans += res
    println(ans)
}