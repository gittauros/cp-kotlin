package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.dao
import com.tauros.cp.dar
import kotlin.math.abs

/**
 * @author tauros
 * 2023/11/22
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    val eps = 1e-7
    repeat(cases) {
        val (c, m, p, v) = rd.nad(4)
        val sum = dar(22)
        fun dfs(rep: Int, prob: Double, iter: DoubleArray, draw: Int) {
            if (draw == 2) {
                sum[rep] += prob
                return
            }
            for (i in 0 until 3) if (iter[i] > eps) {
                val (j, k) = (i + 1) % 3 to (i + 2) % 3
                val low = iter[i] <= v + eps
                val cnt = (if (iter[j] > eps) 1 else 0) + (if (iter[k] > eps) 1 else 0)
                val reduce = if (low) iter[i] else v
                val next = dar(3)
                next[i] = if (low) 0.0 else iter[i] - v
                next[j] = if (iter[j] > eps) iter[j] + reduce / cnt else 0.0
                next[k] = if (iter[k] > eps) iter[k] + reduce / cnt else 0.0
                dfs(rep + 1, iter[i] * prob, next, i)
            }
        }
        dfs(0, 1.0, dao(c, m, p), -1)
        val ans = sum.withIndex().sumOf { (rep, prob) -> rep * prob }
        wt.printf("%.8f\n", ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}