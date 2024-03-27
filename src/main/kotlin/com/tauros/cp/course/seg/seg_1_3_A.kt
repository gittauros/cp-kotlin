package com.tauros.cp.course.seg

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
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
    val seg = Seg(n + 1, { ar(it) { Info() } })
    for (i in 0 until n) {
        val num = rd.ni() - 1
        val ans = seg.query(num + 1, n + 1, { sum }, int::plus)
        seg.update(num) { sum = 1 }
        wt.print("$ans ")
    }
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}