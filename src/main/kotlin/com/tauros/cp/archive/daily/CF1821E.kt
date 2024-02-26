package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2024/1/18
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val k = rd.ni()
        val str = rd.ns()

        val stack = iar(str.length)
        var top = -1
        val res = mlo<int>()
        for ((i, c) in str.withIndex()) {
            if (c == '(') stack[++top] = i
            else {
                val j = stack[top--]
                res.add((i - j - 1) / 2)
            }
        }
        res.sort()

        var ans = 0L
        for (i in 0 until res.size - k) ans += res[i]
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}