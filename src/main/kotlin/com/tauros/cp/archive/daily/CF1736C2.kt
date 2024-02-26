package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2023/11/23
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val n = rd.ni()
    val nums = rd.na(n)
    val m = rd.ni()
    val queries = ar(m) { rd.na(2) }

    var (p, q) = 0 to 0
    val reach = iar(n) { -1 }
    val bar = iar(n + 1) { n }
    val ori = lar(n + 1)
    while (p < n) {
        while (q < n && nums[q] >= q - p + 1) {
            if (reach[q] == -1) reach[q] = p
            q++
        }
        if (bar[q] == n) bar[q] = p
        ori[p + 1] = ori[p] + q - p
        p++
    }

    var (l, r) = 0 to 0
    val skip = lar(n + 1)
    var mark = -1
    while (l < n) {
        while (r < n && (mark <= l || nums[r] >= r - l + 1)) {
            mark = maxOf(mark, r - nums[r] + 1)
            r++
        }
        skip[l + 1] = skip[l] + r - l
        l++
    }
    repeat(m) {
        val (i, x) = queries[it][0] - 1 to queries[it][1]
        if (x == nums[i]) {
            wt.println(ori[n])
        } else if (x < nums[i]) {
            val st = i - x + 1
            val first = reach[i]
            val diff = if (first < st) {
                val newSum = (i - 1L) * (st - first) - (first + st - 1L) * (st - first) / 2 + st - first
                val sum = ori[st] - ori[first]
                newSum - sum
            } else 0
            val ans = ori[n] + diff
            wt.println(ans)
        } else {
            val (st, ed) = maxOf(i - x + 1, bar[i], 0) to reach[i] - 1
            if (ed >= 0 && st <= ed) {
                val newSum = skip[ed + 1] - skip[st]
                val sum = ori[ed + 1] - ori[st]
                val diff = newSum - sum
                val ans = ori[n] + diff
                wt.println(ans)
            } else {
                wt.println(ori[n])
            }
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}