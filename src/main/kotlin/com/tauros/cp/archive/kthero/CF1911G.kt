package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.car
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/5
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val k = rd.ni()
    val (s, t) = rd.ns(k).reversedArray() to rd.ns(k).reversedArray()
    val res = iar(k)
    var rem = 0
    for (i in 0 until k) {
        val (u, d) = s[i] - 'a' to t[i] - 'a'
        val sum = u + d + rem
        res[i] = sum % 26
        rem = sum / 26
    }

    val ans = iar(k)
    for (i in k - 1 downTo 0) {
        val cur = rem * 26 + res[i]
        ans[k - 1 - i] = cur / 2
        rem = cur % 2
    }
    val str = buildString { for (num in ans) append('a' + num) }
    wt.println(str)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}