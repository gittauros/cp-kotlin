package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.car
import com.tauros.cp.common.swap
import com.tauros.cp.iao
import com.tauros.cp.iar
import kotlin.math.abs
import kotlin.random.Random

/**
 * @author tauros
 * 2023/12/11
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val str = rd.ns().toCharArray()
        val n = str.size
        val sums = iar(n + 1)
        val last = (n downTo 1).firstOrNull { str[it - 1] == '+' } ?: 0
        val ans = iao(1, 1)
        for (i in 1 .. n) {
            sums[i] = sums[i - 1] + (if (str[i - 1] == '+') 1 else -1)
            if (sums[i] < 0 && last > i) {
                str.swap(i - 1, last - 1)
                ans[0] = i
                ans[1] = last
                break
            }
        }
        for (i in 1 .. n) {
            sums[i] = sums[i - 1] + (if (str[i - 1] == '+') 1 else -1)
            if (sums[i] < 0) {
                wt.println(-1)
                return@repeat
            }
        }
        wt.println("${ans[0]} ${ans[1]}")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}