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
    val seg = Seg(n, { ar(it) { Info() } }) {
        val num = rd.ni()
        sum = if (it % 2 == 0) num else -num
    }
    val m = rd.ni()
    repeat(m) {
        val op = rd.ni()
        if (op == 0) {
            val (i, v) = rd.ni() - 1 to rd.ni()
            seg.update(i) { sum = if (i % 2 == 0) v else -v }
        } else {
            val (l, r) = rd.ni() - 1 to rd.ni() - 1
            val ans = seg.query(l, r + 1, { sum }, int::plus)
            wt.println(if (l % 2 == 0) ans else -ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}