package com.tauros.cp.course

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
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

    data class Info(var v: int, var cnt: int = 1) : SegNonTagNode<Info> {
        override fun update(l: Info, r: Info) {
            v = minOf(l.v, r.v)
            cnt = if (v == l.v) l.cnt else 0
            if (v == r.v) cnt += r.cnt
        }
    }
    val seg = Seg(n, { ar(it) { Info(0) } }) {
        v = rd.ni()
    }
    repeat(m) {
        val op = rd.ni()
        if (op == 1) {
            val (i, v) = rd.na(2)
            seg[i] = Info(v)
        } else {
            val (l, r) = rd.na(2)
            val (v, cnt) = seg.query(l, r, { Info(v, cnt) }) { a, b ->
                val v = minOf(a.v, b.v)
                var cnt = if (v == a.v) a.cnt else 0
                if (v == b.v) cnt += b.cnt
                Info(v, cnt)
            }
            wt.println("$v $cnt")
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}