package com.tauros.cp.course.seg

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.structure.Seg
import com.tauros.cp.structure.SegNonTagNode
import com.tauros.cp.structure.SegTagNode
import com.tauros.cp.structure.SegTree
import com.tauros.cp.structure.SegTreeTagNode

/**
 * @author tauros
 * 2024/3/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val n = rd.ni()

    data class Info(var min: int = n + 1) : SegTagNode<Info, int> {
        override var tag = 0
        override fun tagAvailable() = tag != 0
        override fun clearTag() { tag = 0 }
        override fun acceptTag(other: int) {
            tag += other; min += other
        }
        override fun update(l: Info, r: Info) {
            min = minOf(l.min, r.min)
        }
    }
    val seg = Seg(n, { ar(it) { Info() } }) {
        min = rd.ni()
    }
    val ans = iar(n)
    repeat(n) { i ->
        val idx = seg.first(n, { min }, { it > 0 }, ::minOf) - 1
        ans[idx] = n - i
        seg.update(idx, n, -1)
        seg.update(idx) { min = n + 1 }
    }
    for (res in ans) wt.print("$res ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}