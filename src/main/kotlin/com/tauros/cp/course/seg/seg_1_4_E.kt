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
    // https://codeforces.com/edu/course/2/lesson/4/4/practice/contest/274684/problem/E
    // 暴力查即可，因为最多只有n+q个点能被查出来
    val (n, m) = rd.ni() to rd.ni()

    val inf = 0x3f3f3f3f
    data class Info(var min: int = inf) : SegNonTagNode<Info> {
        override fun update(l: Info, r: Info) {
            min = minOf(l.min, r.min)
        }
    }
    val seg = Seg(n, { ar(it) { Info() } })
    repeat(m) {
        val op = rd.ni()
        if (op == 1) {
            val (i, h) = rd.ni() to rd.ni()
            seg.update(i) { min = h }
        } else {
            val (l, r, p) = rd.na(3)
            var ans = 0
            while (true) {
                val i = seg.last(l, { min }, { it > p }, ::minOf)
                if (i >= r) break
                ans += 1
                seg.update(i) { min = inf }
            }
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}