package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.long
import com.tauros.cp.iar
import com.tauros.cp.mso

/**
 * @author tauros
 * 2024/1/12
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
        val str = rd.ns(n)
        val pre = iar(n)
        val suf = iar(n)

        var pos = -1
        for (i in 0 until n) {
            if (str[i] == '0') pos = i
            pre[i] = pos
        }
        pos = n
        for (i in n - 1 downTo 0) {
            if (str[i] == '1') pos = i
            suf[i] = pos
        }

        val set = buildSet {
            repeat(m) {
                val (l, r) = rd.ni() - 1 to rd.ni() - 1
                val (cl, cr) = suf[l] to pre[r]
                if (cl > cr) {
                    add(n to n)
                } else {
                    add(cl to cr)
                }
            }
        }
        wt.println(set.size)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}