package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.DSU
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/11/21
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m, q) = rd.na(3)
    val str = rd.ns(n)
    val dsu = DSU(n + 1)
    val end: Int
    val idx = buildList {
        repeat(m) {
            val (l, r) = rd.ni() - 1 to rd.ni() - 1
            var iter = dsu.find(l)
            while (iter <= r) {
                add(iter)
                dsu.merge(iter, iter + 1)
                iter = dsu.find(l)
            }
        }
        end = size
        var iter = dsu.find(0)
        while (iter < n) {
            add(iter)
            dsu.merge(iter, iter + 1)
            iter = dsu.find(0)
        }
    }.toIntArray()

    var total = 0
    val (dot, pos) = iar(n) to iar(n)
    for (i in 0 until n) {
        pos[idx[i]] = i
        dot[i] = str[idx[i]] - '0'
        total += dot[i]
    }
    var inner = (0 until minOf(end, total)).sumOf { dot[it] }

    repeat(q) {
        val query = rd.ni() - 1
        val i = pos[query]
        val diff = if (dot[i] == 1) -1 else 1
        val preBound = minOf(end, total)
        total += diff
        val bound = minOf(end, total)
        if (bound >= preBound) {
            dot[i] += diff
            if (bound > preBound) inner += dot[preBound]
            if (i < preBound) inner += diff
        } else {
            inner -= dot[bound]
            if (i < bound) inner += diff
            dot[i] += diff
        }

        val ans = bound - inner
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}