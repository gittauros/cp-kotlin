package com.tauros.cp.archive.daily

import com.tauros.cp.common.mint
import com.tauros.cp.common.toMInt
import com.tauros.cp.common.withMod

/**
 * @author tauros
 * 2024/3/3
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/584/B
    // 排列组合，容斥一下，考虑 1 + 2 + 3 = 6 和 2 + 2 + 2 = 6 即可
    val n = readln().toInt()
    withMod(1e9.toInt() + 7) {
        val m3 = 3.toMInt()
        val single = m3.pow(3) - (3 * 2 * 1 + 1)
        var rest = 3 * n - 3
        var pre = mint.ONE
        var ans = mint.ZERO
        for (i in 0 until n) {
            val res = pre * single * m3.pow(rest)
            ans += res
            pre *= 3 * 2 * 1 + 1
            rest -= 3
        }
        println(ans)
    }
}