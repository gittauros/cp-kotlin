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
    val (n, m) = rd.ni() to rd.ni()

    data class Info(var sum: int = 0) : SegNonTagNode<Info> {
        override fun update(l: Info, r: Info) {
            sum = l.sum + r.sum
        }
    }
    val seg = Seg(n, { ar(it) { Info() } }) { sum = rd.ni() }
    repeat(m) {
        val op = rd.ni()
        if (op == 1) {
            val i = rd.ni()
            seg.update(i) { sum = 1 - sum }
        } else {
            val k = rd.ni() + 1
            val i = seg.last(0, { sum }, { it < k }, int::plus)
            wt.println(i)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}