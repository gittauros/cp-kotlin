package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iar

/**
 * @author tauros
 * 2025/1/2
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val (m, n, k, t) = rd.na(4)
    val a = rd.na(m).sorted()
    data class Trap(val l: int, val r: int, val d: int)
    val traps = buildList { repeat(k) { add(Trap(rd.ni(), rd.ni(), rd.ni())) } }.sortedBy { it.l }

    val ans = findFirst(m) {
        val pivot = a[it]
        var (st, ed) = 0 to -1
        var time = 0L
        for ((l, r, d) in traps) {
            if (pivot < d) {
                if (l <= ed) ed = maxOf(r, ed)
                else {
                    time += (ed - st + 1) * 2L
                    st = l; ed = r
                }
            }
        }
        time += (ed - st + 1) * 2L
        time += n + 1
        time <= t
    }
    wt.println(m - ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}