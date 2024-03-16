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
 * 2024/3/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val grid = ar(2) { rd.ns(n) }

        val ops = iao(0, 1, 0, -1, 0)
        val vis = ar(2) { bar(n) }
        val able = ar(2) { bar(n) }
        val q = dq<IIP>()
        q.addLast(0 to 0)
        able[0][0] = true
        while (q.isNotEmpty()) {
            val (r, c) = q.removeFirst()
            for (o in 0 until 4) {
                val (nr, nc) = r + ops[o] to c + ops[o + 1]
                if (nr !in 0 until 2 || nc !in 0 until n || vis[nr][nc]) continue
                vis[nr][nc] = true; able[nr][nc] = true
                val col = if (grid[nr][nc] == '<') nc - 1 else nc + 1
                able[nr][col] = true
                q.addLast(nr to col)
            }
        }
        wt.println(if (able[1][n - 1]) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}