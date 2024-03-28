package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.long
import com.tauros.cp.graph.Graph
import com.tauros.cp.graph.withTreeInfo
import com.tauros.cp.structure.Seg
import com.tauros.cp.structure.SegTagNode

/**
 * @author tauros
 * 2024/3/28
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()

    val vtx = rd.na(n)
    val graph = Graph(n, (n - 1) * 2)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
    }

    data class Info(var col: long = 0) : SegTagNode<Info, long> {
        override var tag = 0L
        override fun tagAvailable() = tag != 0L
        override fun clearTag() { tag = 0L }
        override fun acceptTag(other: long) {
            tag = other; col = other
        }
        override fun update(l: Info, r: Info) {
            col = l.col or r.col
        }
    }
    graph.withTreeInfo(0) {
        val seg = Seg(n, { ar(it) { Info() } }) {
            col = 1L shl vtx[ori[it]] - 1
        }
        repeat(m) {
            val (op, v) = rd.ni() to rd.ni() - 1
            val range = dfnRange(v)
            val (st, ed) = range.first to range.last
            if (op == 1) {
                val c = rd.ni() - 1
                seg.update(st, ed + 1, 1L shl c)
            } else {
                val col = seg.query(st, ed + 1, { col }) { l, r -> l or r}
                val ans = col.countOneBits()
                wt.println(ans)
            }
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}