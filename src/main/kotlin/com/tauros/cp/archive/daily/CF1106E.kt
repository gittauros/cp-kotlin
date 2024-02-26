package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.structure.IntHeap

/**
 * @author tauros
 * 2024/2/6
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1106/E
    val (n, m, k) = rd.na(3)
    data class Envelop(val s: int, val t: int, val d: int, val w: int) {
        operator fun compareTo(other: Envelop) = if (w == other.w) -d.compareTo(other.d) else -w.compareTo(other.w)
    }
    val envelopes = ar(k) { Envelop(rd.ni() - 1, rd.ni() - 1, rd.ni() - 1, rd.ni()) }

    val inner = IntHeap(iar(k)) { i, j -> envelopes[i].compareTo(envelopes[j]) }
    val outer = IntHeap(iar(k) { it }) { i, j -> envelopes[i].s.compareTo(envelopes[j].s) }.apply { build() }
    val choose = iar(n)
    for (i in 0 until n) {
        while (outer.isNotEmpty() && envelopes[outer.peek()].s <= i) inner.offer(outer.poll())
        while (inner.isNotEmpty() && envelopes[inner.peek()].t < i) inner.poll()
        choose[i] = if (inner.isNotEmpty()) inner.peek() else -1
    }

    val dp = ar(2) { lar(n) }
    var cur = 0
    for (rest in 0 .. m) {
        val pre = cur
        cur = 1 - cur
        for (i in n - 1 downTo 0) {
            val envelope = if (choose[i] == -1) null else envelopes[choose[i]]
            if (envelope == null) {
                dp[cur][i] = if (i == n - 1) 0 else dp[cur][i + 1]
                continue
            }
            val (_, _, d, w) = envelope
            val not = (if (d + 1 >= n) 0 else dp[cur][d + 1]) + w
            if (rest == 0) {
                dp[cur][i] = not
            } else {
                dp[cur][i] = minOf(not, if (i + 1 >= n) 0 else dp[pre][i + 1])
            }
        }
    }
    wt.println(dp[cur][0])
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}