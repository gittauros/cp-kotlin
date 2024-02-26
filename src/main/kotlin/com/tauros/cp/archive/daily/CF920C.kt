package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2023/12/4
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val nums = rd.na(n).map { it - 1 }
    val str = rd.ns(n - 1)
    var iter = 0
    while (iter < n) {
        var (cl, cr) = n to -1
        val st = iter
        while (iter < n - 1 && str[iter] == '1') {
            cl = minOf(cl, nums[iter])
            cr = maxOf(cr, nums[iter])
            iter++
        }
        cl = minOf(cl, nums[iter])
        cr = maxOf(cr, nums[iter])
        val ed = iter++
        if (cl != st || cr != ed) {
            wt.println("NO")
            return
        }
    }
    wt.println("YES")
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}