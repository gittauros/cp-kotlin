package com.tauros.cp.archive.dp

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/14
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        val suf = iar(n + 2)
        for (i in n downTo 1) {
            suf[i] = if (i < n && nums[i - 1] >= nums[i]) suf[i + 1] + 1 else suf[i + 1]
        }

        var (ans, dp) = suf[1] to 0
        for (i in 1 .. n) {
            val newDp = if (i == 1 || nums[i - 1] >= nums[i - 2]) dp + 1 else dp
            dp = newDp
            ans = minOf(ans, dp + suf[i + 1])
        }

        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}