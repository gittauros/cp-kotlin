package com.tauros.cp.course.seg

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.long
import com.tauros.cp.iar
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

    val cap = 40
    class Info(val cnt: IntArray = iar(cap), var inversions: long = 0) : SegNonTagNode<Info> {
        override fun update(l: Info, r: Info) {
            for (i in 0 until cap) cnt[i] = l.cnt[i] + r.cnt[i]
            inversions = l.inversions + r.inversions
            var pre = 0L
            for (i in cap - 1 downTo 0) {
                inversions += pre * r.cnt[i]
                pre += l.cnt[i]
            }
        }
    }
    val nums = rd.na(n).map { it - 1 }.toIntArray()
    val seg = Seg(n, { ar(it) { Info() } }) {
        cnt[nums[it]] = 1
    }
    repeat(q) {
        val op = rd.ni()
        if (op == 1) {
            val (l, r) = rd.ni() - 1 to rd.ni() - 1
            val ans = seg.query(l, r + 1, { this }) { a, b ->
                Info().also { it.update(a, b) }
            }
            wt.println(ans.inversions)
        } else {
            val (i, v) = rd.ni() - 1 to rd.ni() - 1
            seg.update(i) {
                cnt[nums[i]] = 0
                cnt[v] = 1
            }
            nums[i] = v
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}