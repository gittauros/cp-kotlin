package com.tauros.cp.archive.daily

import com.tauros.cp.ar
import com.tauros.cp.common.char
import com.tauros.cp.iar
import com.tauros.cp.string.pmt

/**
 * @author tauros
 * 2024/3/28
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1163/D
    // 想了一年没想到想出来了
    // dp的状态是kmp匹配到的位置即可
    val str = readln()
    val s = readln()
    val t = readln()

    val inf = 0x3f3f3f3f
    val (n, m) = s.length to t.length
    val (sPmt, tPmt) = s.pmt() to t.pmt()

    val dp = ar(2) { iar(n * m) { -inf } }
    dp[0][0] = 0
    var cur = 0
    for (c in str) {
        val pre = cur; cur = 1 - cur
        dp[cur].fill(-inf)

        fun transfer(c: char) {
            val ni = iar(n) {
                var iter = it
                while (iter > 0 && s[iter] != c) iter = sPmt[iter - 1]
                if (s[iter] == c) iter + 1 else iter
            }
            val nj = iar(m) {
                var iter = it
                while (iter > 0 && t[iter] != c) iter = tPmt[iter - 1]
                if (t[iter] == c) iter + 1 else iter
            }
            for (i in 0 until n) for (j in 0 until m) {
                val f = dp[pre][i * m + j] + (if (ni[i] == n) 1 else 0) + (if (nj[j] == m) -1 else 0)
                val next = (if (ni[i] == n) sPmt.last() else ni[i]) * m + (if (nj[j] == m) tPmt.last() else nj[j])
                dp[cur][next] = maxOf(dp[cur][next], f)
            }
        }
        if (c == '*') {
            ('a' .. 'z').onEach { transfer(it) }
        } else transfer(c)
    }
    val ans = dp[cur].max()
    println(ans)
}