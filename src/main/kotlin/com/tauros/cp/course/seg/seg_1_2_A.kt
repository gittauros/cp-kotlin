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

    data class Info(var pre: long = 0, var suf: long = 0, var max: long = 0, var sum: long = 0) : SegNonTagNode<Info> {
        override fun update(l: Info, r: Info) {
            max = maxOf(l.max, r.max, l.suf + r.pre, 0)
            sum = l.sum + r.sum
            pre = maxOf(l.pre, l.sum + r.pre, 0)
            suf = maxOf(r.suf, r.sum + l.suf, 0)
        }
    }
    val seg = Seg(n, { ar(it) { Info() } }) {
        val v = rd.nl()
        pre = maxOf(v, 0); suf = maxOf(v, 0); max = maxOf(v, 0); sum = v
    }
    wt.println(seg.queryAll { max })
    repeat(m) {
        val (i, v) = rd.ni() to rd.nl()
        seg.update(i) { pre = maxOf(v, 0); suf = maxOf(v, 0); max = maxOf(v, 0); sum = v }
        wt.println(seg.queryAll { max })
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}