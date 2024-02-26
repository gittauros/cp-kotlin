package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/1/18
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val nums = rd.na(n)

    val pos = ar(2) { iar(n + 1) { -1 } }
    val sum = ar(2) { iar(n) }
    val cnt = iar(2)
    for ((i, num) in nums.withIndex()) {
        cnt[num - 1] += 1
        pos[num - 1][cnt[num - 1]] = i
        sum[0][i] = cnt[0]
        sum[1][i] = cnt[1]
    }

    val ans = buildList {
        for (t in 1 .. n) {
            val sets = iar(2)
            var (iter, cur) = -1 to -1
            while (true) {
                val (i0, i1) = (if (iter < 0) 0 else sum[0][iter]) + t to (if (iter < 0) 0 else sum[1][iter]) + t
                val (p, q) = (if (i0 > n) -1 else pos[0][i0]) to (if (i1 > n) -1 else pos[1][i1])
                if (p == -1 && q == -1) break
                iter = if (q == -1 || p in 0 until q) {
                    cur = 0
                    p
                } else {
                    cur = 1
                    q
                }
                sets[cur] += 1
            }
            if (iter == n - 1 && sets[cur] > sets[1 - cur]) {
                add(sets[cur] to t)
            }
        }
    }.sortedWith { a, b -> if (a.first == b.first) a.second.compareTo(b.second) else a.first.compareTo(b.first) }
    wt.println(ans.size)
    for ((s, t) in ans) {
        wt.println("$s $t")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}