package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.car
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/20
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, l, k) = rd.na(3)
    val cap = 26
    val cnt = iar(cap)
    repeat(n * l) { cnt[rd.nc() - 'a'] += 1 }

    val chs = ar(n) { car(l) { '*' } }

    var stRow = 0
    var (cur, col) = 0 to 0
    while (col < l) {
        var iter = stRow
        while (cnt[cur] > 0 && iter < k) {
            chs[iter][col] = 'a' + cur
            cnt[cur] -= 1
            iter += 1
        }
        if (cnt[cur] == 0) cur += 1
        if (iter < k) stRow = iter
        else col += 1
    }

    for (r in 0 until n) for (c in 0 until l) if (chs[r][c] == '*') {
        while (cnt[cur] <= 0) cur += 1
        chs[r][c] = 'a' + cur
        cnt[cur] -= 1
    }

    for (i in 0 until n) wt.println(String(chs[i]))
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}