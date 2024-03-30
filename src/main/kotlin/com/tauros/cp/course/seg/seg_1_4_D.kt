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
    val (n, q) = rd.ni() to rd.ni()

    class Info(var mask: long = 0L) : SegNonTagNode<Info> {
        override fun update(l: Info, r: Info) {
            mask = l.mask or r.mask
        }
    }
    val seg = Seg(n, { ar(it) { Info() } }) {
        val num = rd.ni() - 1
        mask = 1L shl num
    }
    repeat(q) {
        val op = rd.ni()
        if (op == 1) {
            val (l, r) = rd.ni() - 1 to rd.ni() - 1
            val ans = seg.query(l, r + 1, { mask }, long::or)
            wt.println(ans.countOneBits())
        } else {
            val (i, v) = rd.ni() - 1 to rd.ni() - 1
            seg.update(i) {
                mask = 1L shl v
            }
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}