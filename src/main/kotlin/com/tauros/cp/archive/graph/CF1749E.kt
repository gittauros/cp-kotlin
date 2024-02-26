package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1749/problem/E
    // 平面图的最短路和最小割互相转换
    // https://www.luogu.com.cn/problem/P4001
    // https://codeforces.com/gym/104821/problem/E
    // https://www.luogu.com.cn/problem/P7916
    val cases = rd.ni()
    val EPT = -1 to -1
    repeat(cases) {
        val (n, m) = rd.ni() to rd.ni()
        val grid = ar(n) { rd.ns(m) }

        val checkOps = iao(0, 1, 0, -1, 0)
        fun available(i: int, j: int) = (0 until 4).all { o ->
            val (ni, nj) = i + checkOps[o] to j + checkOps[o + 1]
            ni !in 0 until n || nj !in 0 until m || grid[ni][nj] == '.'
        }

        val moveOps = iao(1, 1, -1, -1, 1)
        val dist = ar(n) { iar(m) { INF } }
        val vis = ar(n) { bar(m) }
        val pre = ar(n) { ar<IIP>(m) { EPT } }
        val dq = dq<Pair<int, IIP>>()
        for (i in 0 until n) if (grid[i][0] == '#' || grid[i][0] == '.' && available(i, 0)) {
            dist[i][0] = if (grid[i][0] == '.') 1 else 0
            val st = i to 0
            if (dist[i][0] == 0) dq.addFirst(0 to st)
            else dq.addLast(1 to st)
        }
        while (dq.isNotEmpty()) {
            val (curDist, p) = dq.removeFirst()
            val (r, c) = p
            if (vis[r][c]) continue
            vis[r][c] = true
            for (o in 0 until  4) {
                val (nr, nc) = r + moveOps[o] to c + moveOps[o + 1]
                if (nr !in 0 until n || nc !in 0 until m || !available(nr, nc)) continue
                val add = if (grid[nr][nc] == '#') 0 else 1
                if (curDist + add < dist[nr][nc]) {
                    dist[nr][nc] = curDist + add
                    pre[nr][nc] = p
                    if (add == 0) {
                        dq.addFirst(curDist to (nr to nc))
                    } else {
                        dq.addLast(curDist + add to (nr to nc))
                    }
                }
            }
        }

        val idx = (0 until n).minByOrNull { dist[it][m - 1] } ?: 0
        if (dist[idx][m - 1] == INF) {
            wt.println("NO")
            return@repeat
        }
        wt.println("YES")
        var iter = idx to m - 1
        while (iter != EPT) {
            val (i, j) = iter
            grid[i][j] = '#'
            iter = pre[i][j]
        }
        for (i in 0 until n) {
            for (j in 0 until m) wt.print(grid[i][j])
            wt.println()
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}