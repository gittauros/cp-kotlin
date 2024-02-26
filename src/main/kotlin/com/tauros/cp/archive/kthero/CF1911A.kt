package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.mlo
import com.tauros.cp.mmo

/**
 * @author tauros
 * 2023/12/5
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
        val cnt = mmo<int, MutableList<int>>()
        for ((i, num) in nums.withIndex()) {
            cnt.computeIfAbsent(num) { mlo() }.add(i)
        }
        for ((_, pos) in cnt) {
            if (pos.size == 1) {
                wt.println(pos[0] + 1)
                return@repeat
            }
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}