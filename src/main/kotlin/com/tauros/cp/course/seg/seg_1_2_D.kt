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

    data class Info(var max: int = 0) : SegNonTagNode<Info> {
        override fun update(l: Info, r: Info) {
            max = maxOf(l.max, r.max)
        }
    }
    val seg = Seg(n, { ar(it) { Info() } }) { max = rd.ni() }
    repeat(m) {
        val op = rd.ni()
        if (op == 1) {
            val (i, v) = rd.ni() to rd.ni()
            seg.update(i) { max = v }
        } else {
            val (x, l) = rd.ni() to rd.ni()
            val i = seg.last(l, { max }, { it < x }, ::maxOf)
            wt.println(if (i >= n) -1 else i)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}