package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int

/**
 * @author tauros
 * 2024/1/30
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) { case ->
        val n = rd.ni()
        val a = rd.na(n)
        val b = rd.na(n)

        val fail = (0 until n).any { a[it] > b[it] || a[it] < b[it] && b[it] > b[(it + 1) % n] + 1 }
        wt.println(if (!fail) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}