package com.tauros.cp.archive.daily

import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/4
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1185/C2
    // 值域就100，暴力就好了
    val (n, m) = readln().split(" ").map { it.toInt() }
    val time = readln().split(" ").map { it.toInt() }

    val cnt = iar(101)
    val ans = iar(n)
    var sum = 0
    for (i in 0 until n) {
        var (iter, res, cur) = iao(sum, 0, 100)
        while (iter + time[i] > m) {
            val cut = iter + time[i] - m
            val need = (cut + cur - 1) / cur
            val has = minOf(need, cnt[cur])
            iter -= has * cur; res += has
            cur -= 1
        }
        ans[i] = res
        sum += time[i]
        cnt[time[i]] += 1
    }
    println(ans.joinToString(" "))
}