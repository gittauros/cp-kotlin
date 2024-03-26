package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import java.util.TreeSet

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
    val lightSet = TreeSet<int>()
    val gtSet = TreeSet<int>()
    val ltSet = TreeSet<int>()
    var (sum, gtSum) = 0L to 0L
    repeat(n) {
        val (tp, d) = rd.ni() to rd.ni()
        sum += d
        if (tp == 1) {
            if (d < 0) lightSet.remove(-d)
            else lightSet.add(d)
        }
        if (d < 0) {
            if (gtSet.contains(-d)) {
                gtSet.remove(-d)
                gtSum += d
            } else ltSet.remove(-d)
        } else {
            ltSet.add(d)
        }
        while (ltSet.isNotEmpty() && gtSet.isNotEmpty() && ltSet.last() > gtSet.first()) {
            val pow = ltSet.last().also { ltSet.remove(it) }
            gtSet.add(pow); gtSum += pow
        }
        while (gtSet.size != lightSet.size) {
            if (gtSet.size > lightSet.size) {
                val pow = gtSet.first().also { gtSet.remove(it) }
                gtSum -= pow
                ltSet.add(pow)
            } else {
                val pow = ltSet.last().also { ltSet.remove(it) }
                gtSum += pow
                gtSet.add(pow)
            }
        }
        val first = lightSet.firstOrNull()
        val ans = if (first == null) sum
        else if (first in gtSet) sum + gtSum - first + (ltSet.lastOrNull() ?: 0)
        else sum + gtSum
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}