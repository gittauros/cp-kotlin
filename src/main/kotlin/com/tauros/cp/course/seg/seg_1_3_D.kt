package com.tauros.cp.course.seg

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.structure.Seg
import com.tauros.cp.structure.SegNonTagNode

/**
 * @author tauros
 * 2024/3/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val n = rd.ni()

    data class Info(var sum: int = 0) : SegNonTagNode<Info> {
        override fun update(l: Info, r: Info) {
            sum = l.sum + r.sum
        }
    }
    val seg = Seg(2 * n, { ar(it) { Info() } })
    val end = iar(n) { -1 }
    val ans = iar(n)
    for (i in 0 until 2 * n) {
        val num = rd.ni() - 1
        if (end[num] == -1) {
            seg.update(i) { sum += 1 }
            end[num] = i
        } else {
            val res = seg.query(end[num] + 1, i + 1, { sum }, int::plus)
            ans[num] = res
            seg.update(i) { sum += 1 }
            seg.update(end[num]) { sum -= 2 }
        }
    }
    for (res in ans) wt.print("$res ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}