package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.common.mergeSort
import com.tauros.cp.lar
import com.tauros.cp.runningFold
import com.tauros.cp.structure.bitQuery
import com.tauros.cp.structure.bitUpdateWithIndex

/**
 * @author tauros
 * 2023/12/30
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1042/D
    val (n, t) = rd.ni() to rd.nl()
    val nums = rd.na(n)
    val sums = nums.runningFold(0L, Long::plus).toLongArray()
    // sums[r] - sums[l] < t
    // sums[l] > sums[r] - t
    var ans = 0L
    val tmp = lar(sums.size)
    fun dac(st: int, ed: int) {
        if (ed - st <= 1) return
        val mid = st + ed shr 1
        dac(st, mid)
        dac(mid, ed)

        var (l, r) = st to mid
        while (r < ed) {
            while (l < mid && sums[l] <= sums[r] - t) l++
            ans += mid - l
            r++
        }

        l = st
        r = mid
        var iter = st
        while (l < mid || r < ed) {
            tmp[iter++] = if (r >= ed || l < mid && sums[l] <= sums[r]) sums[l++] else sums[r++]
        }
        tmp.copyInto(sums, st, st, ed)
    }
    dac(0, sums.size)
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}