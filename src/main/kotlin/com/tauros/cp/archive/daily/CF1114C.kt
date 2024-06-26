package com.tauros.cp.archive.daily

/**
 * @author tauros
 * 2024/4/25
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1114/C
    // 收集每个base的质因子个数，拿能取得的最小个数
    val (n, b) = readln().split(" ").map { it.toLong() }
    val primes = buildList {
        var (iter, p) = b to 2L
        while (p * p <= iter) {
            if (iter % p == 0L) {
                var cnt = 0L
                while (iter % p == 0L) {
                    iter /= p; cnt += 1
                }
                add(p to cnt)
            }
            p += 1
        }
        if (iter > 1) add(iter to 1L)
    }

    val res = primes.map { (p, base) ->
        val cap = n / p
        var cnt = 0L
        var iter = p
        while (iter <= cap) {
            cnt += n / iter; iter *= p
        }
        if (iter <= n) {
            cnt += n / iter
        }
        cnt / base
    }
    val ans = res.min()
    println(ans)
}