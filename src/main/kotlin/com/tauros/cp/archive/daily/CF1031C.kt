package com.tauros.cp.archive.daily

import com.tauros.cp.common.findFirst

/**
 * @author tauros
 * 2024/4/20
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1031/C
    // 贪一下，等差数列求和，最多就拿那么多
    val (a, b) = readln().split(" ").map { it.toInt() }
    val (l, r) = minOf(a, b) to maxOf(a, b)

    val (aAns, bAns) = run {
        val tot = l.toLong() + r
        val len = findFirst(tot + 1) { (1 + it) * it / 2 > tot } - 1
        val lenL = findFirst(tot + 1) { (1 + it) * it / 2 > l } - 1
        val lList = (1 .. lenL).toMutableList()
        val rList = (lenL + 1 .. len).toMutableList()
        if ((1 + lenL) * lenL / 2 < l) {
            val diff = l - (1 + lenL) * lenL / 2
            val (i, j) = if (lenL > diff) (lenL + 1 - diff - 1).toInt() to 0 else 0 to (diff + 1 - (lenL + 1)).toInt()
            val tmp = lList[i]; lList[i] = rList[j]; rList[j] = tmp
        }
        if (a == l) lList to rList else rList to lList
    }
    println(aAns.size)
    println(buildString { for (res in aAns) append("$res ") })
    println(bAns.size)
    println(buildString { for (res in bAns) append("$res ") })
}