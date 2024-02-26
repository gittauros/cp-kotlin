package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2023/12/9
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, m, k) = rd.na(3)
        val lower = n % 2 * m / 2
        val upper = m / 2 * n
        if (k !in lower .. upper) {
            wt.println("NO")
            return@repeat
        }
        val success = (k - lower) % 2 == 0
        wt.println(if (success) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}