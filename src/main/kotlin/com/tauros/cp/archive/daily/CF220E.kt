package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.structure.bitQuery
import com.tauros.cp.structure.bitUpdateWithIndex

/**
 * @author tauros
 * 2024/2/18
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, k) = rd.ni() to rd.nl()
    val nums = rd.na(n)

    val sorted = nums.sorted().distinct().toIntArray()
    val discrete = nums.map { num -> findFirst(sorted.size) { sorted[it] >= num } + 1 }.toIntArray()

    fun IntArray.update(pos: int, add: int) = this.bitUpdateWithIndex(pos) { this[it] += add }
    fun IntArray.query(pos: int) = this.bitQuery(pos, 0, int::plus)

    val suf = iar(n + 2)
    var cur = 0L
    for (i in n - 1 downTo 1) {
        cur += suf.query(discrete[i] - 1)
        suf.update(discrete[i], 1)
    }

    val pre = iar(n + 2)
    var (l, r) = 0 to 1
    var ans = 0L
    while (l < n) {
        cur += suf.query(discrete[l] - 1)
        cur += l - pre.query(discrete[l])
        pre.update(discrete[l], 1)
        l += 1
        while (r < n && (r < l || cur > k)) {
            suf.update(discrete[r], -1)
            cur -= suf.query(discrete[r] - 1)
            cur -= l - pre.query(discrete[r])
            r += 1
        }
        if (cur <= k) {
            ans += n - r
        }
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}