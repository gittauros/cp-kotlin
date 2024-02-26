package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.iar
import com.tauros.cp.mlo
import com.tauros.cp.mmo

/**
 * @author tauros
 * 2023/12/6
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
        val seqs = ar(n) { rd.na(m) }
        val hash = mmo<long, MutableList<int>>()
        // i*a[i] is same
        for (i in 0 until n) {
            val res = (0 until m).sumOf { seqs[i][it] * (it + 1L) }
            hash.computeIfAbsent(res) { mlo() }.add(i + 1)
        }
        val (val1, val2) = hash.keys.toList()
        if (hash[val1]!!.size == 1) {
            wt.print(hash[val1]!![0])
            wt.print(" ")
            wt.println(val1 - val2)
        } else {
            wt.print(hash[val2]!![0])
            wt.print(" ")
            wt.println(val2 - val1)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}