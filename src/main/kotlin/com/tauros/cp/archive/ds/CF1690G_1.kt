package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.structure.Seg
import com.tauros.cp.structure.SegTagNode

/**
 * @author tauros
 * 2024/1/26
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1690/G
    // 除了这个解法的线段树操作外，有更简单的做法：
    // https://codeforces.com/contest/1690/submission/159898395
    // 用TreeSet维护火车头，每次操作过后如果查找小于k的i，如果i的速度小于等于当前k的速度，那么不用操作
    // 否则将k放入set，然后逐个查找大于k的坐标，如果速度大于等于k就删掉
    // 由于初始状态最多有n个点，m次操作最多放入m个点，所以最多删除n + min(n, m)个点，因此复杂度是mlogn的
    data class Info(var st: int = -1, var ed: int = -1, var cnt: int = 1, var speed: int = -1) : SegTagNode<Info, int> {
        override var tag: int = -1
        override fun tagAvailable() = tag != -1
        override fun clearTag() { tag = -1 }
        override fun acceptTag(other: int) {
            tag = other; st = other; ed = other; speed = other
            cnt = 1
        }
        override fun update(l: Info, r: Info) {
            st = l.st; ed = r.ed
            cnt = l.cnt + r.cnt - (if (l.ed == r.st) 1 else 0)
            speed = minOf(l.speed, r.speed)
        }
    }
    val cases = rd.ni()
    repeat(cases) {
        val (n, m) = rd.ni() to rd.ni()
        val a = rd.na(n) + iao(-1)

        val minSpeed = a.runningReduce(::minOf).toIntArray()
        val seg = Seg(n + 1, { Array(it) { Info() } }, ) {
            st = minSpeed[it]; ed = minSpeed[it]; cnt = 1; speed = minSpeed[it]
        }

        repeat(m) {
            val (k, d) = rd.ni() - 1 to rd.ni()
            a[k] -= d

            val cur = seg.query(k).speed
            if (a[k] < cur) {
                val ed = seg.last(k, { speed }, { it >= a[k] }, ::minOf)
                seg.update(k, ed, a[k])
            }

            wt.print(seg.queryAll().cnt - 1)
            wt.print(" ")
        }
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}