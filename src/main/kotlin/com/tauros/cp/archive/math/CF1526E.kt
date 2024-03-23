package com.tauros.cp.archive.math

import com.tauros.cp.iar
import com.tauros.cp.math.Comb

/**
 * @author tauros
 * 2024/3/23
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1526/E
    // https://github.com/Yawn-Sean/Daily_CF_Problems/blob/main/daily_problems/2024/03/0323/solution/cf1526e.md
    // 只想到要排出每个字符的大小关系，但是不会排，也不会处理排出来后的等号
    val (n, k) = readln().split(" ").map { it.toInt() }
    val sa = readln().split(" ").map { it.toInt() }.toIntArray()

    val rk = iar(n)
    for (i in 0 until n) rk[sa[i]] = i
    var eqCnt = 0
    for (i in 0 until n - 1) {
        val (cur, nex) = sa[i] to sa[i + 1]
        if (cur + 1 >= n || nex + 1 < n && rk[cur + 1] < rk[nex + 1]) {
            eqCnt += 1
        }
    }
    // 答案就是C(k+eqCnt, n)
    val comb = Comb(k + eqCnt, 998244353)
    val ans = comb.c(k + eqCnt, n)
    println(ans)
}