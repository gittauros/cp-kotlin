package com.tauros.cp.archive.pain

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.dq
import com.tauros.cp.iao
import com.tauros.cp.mmo

/**
 * @author tauros
 * 2024/1/1
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1651/D
    val n = rd.ni()
    val cap = 1e6.toInt() + 1
    val base = cap * 2
    fun compose(x: int, y: int) = (y.toLong() + cap) * base + (x + cap)
    fun decompose(coord: long) = iao((coord % base - cap).toInt(), (coord / base - cap).toInt())
    val coords = buildList {
        repeat(n) {
            add(compose(rd.ni(), rd.ni()))
        }
    }
    val set = coords.toSet()
    val ans = mmo<long, long>()
    val ops = iao(0, -1, 0, 1, 0)
    val q = dq<long>()
    for (coord in coords) {
        val (x, y) = decompose(coord)
        for (o in 0 until 4) {
            val (dx, dy) = ops[o] to ops[o + 1]
            val (nx, ny) = x + dx to y + dy
            val res = compose(nx, ny)
            if (res !in set) {
                ans[coord] = res
                q.addLast(coord)
                break
            }
        }
    }
    while (q.isNotEmpty()) {
        val coord = q.removeFirst()
        val (x, y) = decompose(coord)
        for (o in 0 until 4) {
            val (dx, dy) = ops[o] to ops[o + 1]
            val (nx, ny) = x + dx to y + dy
            val next = compose(nx, ny)
            if (next in set && next !in ans) {
                ans[next] = ans[coord]!!
                q.addLast(next)
            }
        }
    }
    for ((x, y) in coords.map { decompose(ans[it]!!) }) {
        wt.println("$x $y")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}