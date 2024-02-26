package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iao
import com.tauros.cp.iar
import kotlin.math.abs

/**
 * @author tauros
 * 2024/1/4
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
        val commands = ar(n) { rd.na(2) } + iao(INF, 0)

        var ans = 0
        var (cur, stop) = 0 to 0
        for (i in 0 until n) {
            if (cur == stop) stop = commands[i][1]
            val (st, ed) = commands[i][0] to commands[i + 1][0]
            val len = if (ed == INF) 2 * INF else (ed - st)
            val move = minOf(len, abs(cur - stop))
            val next = cur + if (stop < cur) -move else move
            val expect = commands[i][1]
            if (expect in minOf(cur, next) .. maxOf(cur, next)) ans++
            cur = next
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}