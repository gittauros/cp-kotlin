package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.mso

/**
 * @author tauros
 * 2024/2/16
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val a = rd.nal(n)
        val d = rd.na(n)

        val (l, r) = iar(n) { it - 1 } to iar(n) { it + 1 }
        val deleted = bar(n)
        val ans = buildList {
            val h = lar(n)
            for (i in 0 until n) {
                if (i > 0) h[i - 1] += a[i]
                if (i < n - 1) h[i + 1] += a[i]
            }
            val q = mso<int>()
            for (i in 0 until n) if (h[i] > d[i]) q.add(i)

            while (q.isNotEmpty()) {
                add(q.size)
                for (i in q) {
                    if (l[i] >= 0) h[l[i]] -= a[i]
                    if (r[i] < n) h[r[i]] -= a[i]
                    if (r[i] < n && l[i] >= 0) {
                        h[l[i]] += a[r[i]]
                        h[r[i]] += a[l[i]]
                    }
                    if (r[i] < n) l[r[i]] = l[i]
                    if (l[i] >= 0) r[l[i]] = r[i]
                    deleted[i] = true
                }
                repeat(q.size) {
                    val i = q.first()
                    if (l[i] >= 0 && !deleted[l[i]] && h[l[i]] > d[l[i]]) q.add(l[i])
                    if (r[i] < n && !deleted[r[i]] && h[r[i]] > d[r[i]]) q.add(r[i])
                    q.remove(i)
                }
            }

            while (size < n) add(0)
        }

        for (res in ans) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}