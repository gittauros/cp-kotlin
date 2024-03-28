package com.tauros.cp.course.seg

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.long
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

    data class Info(var sum: long = 0) : SegNonTagNode<Info> {
        override fun update(l: Info, r: Info) {
            sum = l.sum + r.sum
        }
    }
    val seg = Seg(n + 1, { ar(it) { Info() } })
    repeat(m) {
        val op = rd.ni()
        if (op == 1) {
            val (l, r, v) = rd.na(3)
            seg.update(l) { sum += v }
            seg.update(r) { sum -= v }
        } else {
            val i = rd.ni()
            val ans = seg.query(0, i + 1, { sum }, long::plus)
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}