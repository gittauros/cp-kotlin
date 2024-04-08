package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.gcd

/**
 * @author tauros
 * 2024/4/8
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/492/D
    val (n, x, y) = rd.na(3)
    val nums = rd.na(n)

    val gcd = gcd(x, y)
    val loop = x / gcd + y / gcd
    for (num in nums) {
        val rest = num % loop
        if (rest == 0 || rest == loop - 1) {
            wt.println("Both")
            continue
        }
        val xCnt = findFirst(1e9.toInt()) { it.toLong() * y / x + it >= rest }
        val total = xCnt.toLong() * y / x + xCnt
        if (total == rest.toLong()) {
            wt.println("Vanya")
        } else {
            wt.println("Vova")
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}