package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2023/11/20
 */
private val bufCap = 65536

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)
        var ans = 0L
        var pre = nums.last()
        for (i in n - 2 downTo 0) {
            val num = nums[i]
            if (num <= pre) {
                pre = num
                continue
            }
            val x = (num + pre - 1) / pre
            ans += x - 1
            pre = num / x
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}