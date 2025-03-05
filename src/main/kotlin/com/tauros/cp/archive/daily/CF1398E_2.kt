package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.structure.SkipList

/**
 * @author tauros
 * 2024/3/26
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1398/E
    val n = rd.ni()
    val lightList = SkipList<int> { a, b -> a.compareTo(b) }
    val gtList = SkipList<int> { a, b -> a.compareTo(b) }
    val ltList = SkipList<int> { a, b -> a.compareTo(b) }
    var (sum, gtSum) = 0L to 0L
    repeat(n) {
        val (tp, d) = rd.ni() to rd.ni()
        sum += d
        if (tp == 1) {
            if (d < 0) lightList.remove(-d)
            else lightList.add(d)
        }
        if (d < 0) {
            if (gtList.contains(-d)) {
                gtList.remove(-d)
                gtSum += d
            } else ltList.remove(-d)
        } else {
            ltList.add(d)
        }
        while (ltList.isNotEmpty() && gtList.isNotEmpty() && ltList.last() > gtList.first()) {
            val pow = ltList.last().also { ltList.remove(it) }
            gtList.add(pow); gtSum += pow
        }
        while (gtList.size != lightList.size) {
            if (gtList.size > lightList.size) {
                val pow = gtList.first().also { gtList.remove(it) }
                gtSum -= pow
                ltList.add(pow)
            } else {
                val pow = ltList.last().also { ltList.remove(it) }
                gtSum += pow
                gtList.add(pow)
            }
        }
        val first = lightList.firstOrNull()
        val ans = if (first == null) sum
        else if (first in gtList) sum + gtSum - first + (ltList.lastOrNull() ?: 0)
        else sum + gtSum
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}