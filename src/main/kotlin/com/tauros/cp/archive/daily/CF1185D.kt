package com.tauros.cp.archive.daily

import com.tauros.cp.common.int
import com.tauros.cp.common.string
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/8
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1185/D
    // 删一个数其余数排序后能成为等差数列
    val n = readln().toInt()
    val nums = readln().trim().split(" ").map(string::toInt).toIntArray()
    if (n < 3) {
        println("1")
        return
    }

    val sorted = (0 until n).sortedBy { nums[it] }.toIntArray()
    val pred = iar(n + 1) { -2 }
    for (i in 2 .. n) {
        val diff = nums[sorted[i - 1]] - nums[sorted[i - 2]]
        pred[i] = if (pred[i - 1] == -2 || pred[i - 1] == diff) diff else -1
    }
    if (pred[n - 1] != -1) {
        println(sorted[n - 1] + 1)
        return
    }

    var (ans, suf) = -1 to -2
    for (i in n - 1 downTo 1) {
        if (suf == -1) break
        if (i > 1) {
            val diff = nums[sorted[i]] - nums[sorted[i - 2]]
            if ((pred[i - 1] == diff || pred[i - 1] == -2) && (suf == diff || suf == -2)) {
                ans = sorted[i - 1] + 1
                break
            }
        } else {
            ans = sorted[0] + 1
        }
        val diff = nums[sorted[i]] - nums[sorted[i - 1]]
        suf = if (suf == -2 || suf == diff) diff else -1
    }
    println(ans)
}