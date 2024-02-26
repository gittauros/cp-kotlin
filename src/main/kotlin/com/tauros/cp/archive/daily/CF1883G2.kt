package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/1/30
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, m) = rd.na(2)
        val a = rd.na(n - 1).sortedArray()
        val b = rd.na(n).sortedArray()

        val pre = ar(n - 1) { iao(0, -1) }
        var (i, j) = 0 to 0
        while (i < n - 1) {
            if (i > 0) {
                pre[i][0] = pre[i - 1][0]
                pre[i][1] = pre[i - 1][1]
            }
            while (j < n && b[j] <= a[i]) j++
            if (j < n) {
                pre[i][0] += 1
                pre[i][1] = j++
            }
            i += 1
        }

        val suf = ar(n - 1) { iao(0, n) }
        i = n - 2; j = n - 1
        while (i >= 0) {
            if (i < n - 2) {
                suf[i][0] = suf[i + 1][0]
                suf[i][1] = suf[i + 1][1]
            }
            if (j >= 0 && a[i] < b[j]) {
                suf[i][0] += 1
                suf[i][1] = j--
            }
            i -= 1
        }

        val left = if (suf[0][1] > 0) {
            val boundA = minOf(m, a[0])
            val boundB = b[suf[0][1] - 1] - 1
            boundA * (suf[0][0] + 1L) - maxOf(boundA - boundB, 0)
        } else minOf(m, a[0]) * suf[0][0].toLong()
        val right = if (m <= a[n - 2]) 0L else if (pre[n - 2][1] < n - 1) {
            val boundA = m - a[n - 2]
            val boundB = maxOf(b[n - 1] - 1, a[n - 2])
            boundA * (pre[n - 2][0] + 1L) - maxOf(m - boundB, 0)
        } else (m - a[n - 2]) * pre[n - 2][0].toLong()

        val res = suf[0][0].toLong()
        var ans = left + right
        var k = 1
        while (k < n - 1) {
            if (a[k] > a[k - 1]) {
                if (m <= a[k - 1]) break
                val ed = minOf(m, a[k])
                ans += if (pre[k - 1][1] >= suf[k][1] - 1) {
                    (ed - a[k - 1]) * res
                } else {
                    val boundB = maxOf(a[k - 1], b[suf[k][1] - 1] - 1)
                    (ed - a[k - 1]) * (pre[k - 1][0] + suf[k][0] + 1L) - maxOf(ed - boundB, 0)
                }
            }
            k += 1
        }
        wt.println(m.toLong() * n - ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}