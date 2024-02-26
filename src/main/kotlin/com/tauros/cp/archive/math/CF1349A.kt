package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.gcd
import com.tauros.cp.common.lcm

/**
 * @author tauros
 * 2023/11/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1349/problem/A
    val n = rd.ni()
    val nums = rd.na(n)
    var pre = nums[0].toLong()
    var ans = 0L
    for (i in 1 until n) {
        val curPosGcd = lcm(pre, nums[i].toLong())
        ans = gcd(ans, curPosGcd)
        pre = gcd(pre, nums[i].toLong())
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}