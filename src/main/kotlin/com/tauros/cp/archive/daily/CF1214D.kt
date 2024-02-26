package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.IIP
import com.tauros.cp.dq
import com.tauros.cp.iao

/**
 * @author tauros
 * 2023/12/4
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()
    val grid = ar(n) { bar(m) }
    for (i in 0 until n) for (j in 0 until m) {
        grid[i][j] = rd.nc() == '.'
    }
    val ops = iao(0, 1)
    val (vis, q) = ar(n) { bar(m) } to dq<IIP>()

    val reach = ar(n) { bar(m) }
    vis[n - 1][m - 1] = true
    q.add(n - 1 to m - 1)
    while (q.isNotEmpty()) {
        val size = q.size
        repeat(size) {
            val (r, c) = q.removeFirst()
            reach[r][c] = true
            for (p in 0 until 2) {
                val (nr, nc) = r - ops[p] to c - ops[1 - p]
                if (nr !in 0 until n || nc !in 0 until m || vis[nr][nc] || !grid[nr][nc]) continue
                vis[nr][nc] = true
                q.addLast(nr to nc)
            }
        }
    }

    vis.onEach { it.fill(false) }
    var one = false
    vis[0][0] = true
    q.add(0 to 0)
    while (q.isNotEmpty()) {
        val size = q.size
        val only = size == 1
        val (or, oc) = q.first()
        if (only && !(or == 0 && oc == 0 || or == n - 1 && oc == m - 1)) {
            one = true
        }
        repeat(size) {
            val (r, c) = q.removeFirst()
            for (p in 0 until 2) {
                val (nr, nc) = r + ops[p] to c + ops[1 - p]
                if (nr !in 0 until n || nc !in 0 until m || vis[nr][nc] || !grid[nr][nc] || !reach[nr][nc]) continue
                vis[nr][nc] = true
                q.addLast(nr to nc)
            }
        }
    }

    if (!vis[n - 1][m - 1]) {
        wt.println("0")
    } else {
        wt.println(if (one) "1" else "2")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}