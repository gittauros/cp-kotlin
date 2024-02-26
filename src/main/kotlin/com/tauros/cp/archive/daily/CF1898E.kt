package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.dq
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/11/20
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, m) = rd.ni() to rd.ni()
        val (s1, s2) = rd.ns(n) to rd.ns(m)
        val qs = ar(26) { dq<Int>() }
        for ((i, c) in s1.withIndex()) qs[c - 'a'].addLast(i)
        var success = true
        for (c in s2) {
            val idx = c - 'a'
            if (qs[idx].isEmpty()) {
                success = false
                break
            }
            val pos = qs[idx].removeFirst()
            for (i in 0 until idx) {
                while (qs[i].isNotEmpty() && qs[i].first() < pos) qs[i].removeFirst()
            }
        }
        wt.println(if (success) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}