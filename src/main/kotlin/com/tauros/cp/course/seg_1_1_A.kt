package com.tauros.cp.course

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.structure.Seg
import com.tauros.cp.structure.SegNonTagNode


/**
 * @author tauros
 * 2023/8/20
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()

    class Info(var v: long) : SegNonTagNode<Info> {
        override fun update(l: Info, r: Info) {
            v = l.v + r.v
        }
    }
    val seg = Seg(n, { ar(it) { Info(0) } }) {
        v = rd.nl()
    }
    repeat(m) {
        val op = rd.ni()
        if (op == 1) {
            val (i, v) = rd.na(2)
            seg.update(i) { this.v = v.toLong() }
        } else {
            val (l, r) = rd.na(2)
            val ans = seg.query(l, r, { v }) { a, b -> a + b }
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}