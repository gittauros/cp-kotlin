package com.tauros.cp.archive.meetinthemiddle

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.mlo
import com.tauros.cp.mmo
import com.tauros.cp.structure.DefaultMutableMap
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2023/12/26
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/525/E
    val cap = 1e16.toLong()
    val frac = buildMap {
        var (iter, cur) = 1 to 1L
        while (true) {
            put(iter, cur)
            iter += 1
            val next = cur * iter
            cur = if (next <= cur || next > cap || next / iter != cur) break else next
        }
    }

    val (n, k) = rd.ni() to rd.ni()
    val s = rd.nl()
    val nums = rd.na(n).sorted()

    fun calc(o: int): Map<long, MutableMap<Int, Int>> {
        return buildMap {
            val sub = (0 until n).filter { it % 2 == o }.map { nums[it] }.toIntArray()
            fun dfs(i: int, sum: long, fracCnt: int) {
                if (sum > s || fracCnt > k) return
                if (i == sub.size) {
                    val cnt = computeIfAbsent(sum) { mmo() }
                    cnt[fracCnt] = cnt.getOrDefault(fracCnt, 0) + 1
                    return
                }
                for (state in 0 until if (sub[i] <= frac.size) 3 else 2) {
                    val cur = if (state == 0) 0 else if (state == 1) sub[i].toLong() else frac[sub[i]]!!
                    dfs(i + 1, sum + cur, fracCnt + if (state == 2) 1 else 0)
                }
            }
            dfs(0, 0, 0)
        }
    }

    val leftCnt = calc(0)
    val rightCnt = calc(1)
    var ans = 0L
    for ((lSum, lCnt) in leftCnt) if (lSum <= s) {
        val rSum = s - lSum
        if (rSum !in rightCnt) continue
        val rCnt = rightCnt[rSum]!!
        for ((lfCnt, l) in lCnt) if (lfCnt <= k) {
            for ((rfCnt, r) in rCnt) if (lfCnt + rfCnt <= k) {
                ans += l.toLong() * r
            }
        }
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}