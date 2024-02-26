package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.iar
import kotlin.math.abs
import kotlin.random.Random

/**
 * @author tauros
 * 2023/12/25
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, k, d) = rd.na(3)
        val nums = rd.na(n)
        val v = rd.na(k)

        var ans = 0L
        val ops = nums.clone()
        for (collectDays in 1 .. minOf(d, n * 2)) {
            var res = ops.withIndex().count { (i, num) -> i + 1 == num }.toLong()
            res += (d - collectDays) / 2
            ans = maxOf(ans, res)

            for (i in 0 until v[(collectDays - 1) % v.size]) ops[i] += 1
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}