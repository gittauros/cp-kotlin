package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2023/12/3
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
        val monsters = ar(n) { rd.nal(2) }
        val exp = lar(n) {
            val pre = (it - 1 + n) % n
            maxOf(0L, monsters[it][0] - monsters[pre][1])
        }
        val total = exp.sum()
        var ans = INF_LONG
        for (i in 0 until n) {
            val res = total - exp[i] + monsters[i][0]
            ans = minOf(res, ans)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}