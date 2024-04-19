package com.tauros.cp.archive.pain

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.iao

/**
 * @author tauros
 * 2024/4/17
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1065/C
    // 考虑台阶砍掉的部分，比考虑整体的和更合理
    val (n, k) = rd.ni() to rd.ni()
    val h = rd.na(n)

    val sorted = h.groupBy { it }.map { (num, list) -> iao(num, list.size) }.sortedBy { it[0] }.toTypedArray()
    var ans = 0L
    var (sufRest, sufCnt) = 0L to 0L
    for (i in sorted.lastIndex downTo 1) {
        val (v, cnt) = sorted[i]
        val (nv, _) = sorted[i - 1]
        sufCnt += cnt
        val rect = (v - nv) * sufCnt
        if (sufRest + rect < k) sufRest += rect
        else {
            ans += 1
            sufRest = rect - (k - sufRest) / sufCnt * sufCnt
            val step = k / sufCnt * sufCnt
            ans += sufRest / step
            sufRest %= step
        }
    }
    if (sufRest > 0) ans += 1
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}