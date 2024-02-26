package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.mmo
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2024/2/1
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, cap) = rd.ni() to 26
    val cur = mmo<int, IntArray>()
    val full = (1 shl cap) - 1
    var ans = 0L
    repeat(n) {
        val str = rd.ns()
        var (exists, parity) = 0 to 0
        for (c in str) {
            val bit = 1 shl c - 'a'
            exists = bit or exists
            parity = bit xor parity
        }
        val notExist = cur.computeIfAbsent(parity) { iar(cap) }
        for (i in 0 until cap) if (1 shl i and exists == 0) {
            notExist[i] += 1
            val find = cur[1 shl i xor full xor parity]
            if (find != null) ans += find[i]
        }
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}