package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2023/11/20
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val s1 = rd.ns()
    val s2 = rd.ns()
    var parity = 0
    for (c in s2) if (c == '1') parity = parity xor 1
    val sum = s1.map { it - '0' }.runningFold(0, Int::plus)
    var ans = 0
    for (i in s2.length .. s1.length)
        if (sum[i] - sum[i - s2.length] and 1 == parity) ans++
//    var even = 1
//    for (i in s2.indices) {
//        if (s1[i] != s2[i]) even = 1 xor even
//    }
//    var diff = 0
//    for (i in 0 until s2.lastIndex) {
//        if (s2[i] != s2[i + 1]) diff = 1 xor diff
//    }
//    var ans = even
//    for (i in s2.length .. s1.lastIndex) {
//        even = even xor diff
//        if (s1[i] != s2.last()) even = even xor 1
//        if (s1[i - s2.length] != s2[0]) even = even xor 1
//        ans += even
//    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}