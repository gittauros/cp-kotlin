package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2023/12/7
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val p = rd.nl()
    val d = rd.ni()
    val sums = lar(n + 1)
    for (i in 1 .. n) sums[i] = sums[i - 1] + rd.ni()

    var (head, tail) = 0 to 0
    val q = iar(n + 2)
    var ans = 1
    var r = 1
    for (l in 1 .. n) {
        while (head < tail && q[head] - d + 1 < l) head++
        var seg = if (r - d < l) 0L else sums[r - 1] - sums[l - 1] - (sums[q[head]] - sums[maxOf(q[head] - d, 0)])
        while (r <= n && (r - d < l || seg + sums[r] - sums[r - 1] <= p || sums[r] - sums[l - 1] - (sums[r] - sums[maxOf(r - d, 0)]) <= p)) {
            while (head < tail && sums[r] - sums[maxOf(r - d, 0)] >= sums[q[tail - 1]] - sums[maxOf(q[tail - 1] - d, 0)]) tail--
            q[tail++] = r++
            seg = if (r - d < l) 0L else sums[r - 1] - sums[l - 1] - (sums[q[head]] - sums[maxOf(q[head] - d, 0)])
        }
        ans = maxOf(ans, r - l)
    }
    wt.print(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}