package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.iar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/3/1
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/contest/1937/problem/D
    // pr0 ... pr1 pr2  ... i ...  pl1 pl2 ... pln
    //
    // in        out         in          out
    // pl1 - i + pl1 - pr2 + pl2 - pr2 + pl2 - pr1 + pr3 - pr1
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val str = rd.ns(n)

        val (lpos, rpos) = lar(n + 1) to lar(n + 1)
        for (i in 1 .. n) {
            lpos[i] = lpos[i - 1]; rpos[i] = rpos[i - 1]
            if (str[i - 1] == '>') rpos[i] += i.toLong()
            else lpos[i] += i.toLong()
        }

        val (lcnt, rcnt) = iar(n + 1) to iar(n + 1)
        for (i in 1 .. n) {
            rcnt[i] = rcnt[i - 1]; lcnt[i] = lcnt[i - 1]
            if (str[i - 1] == '>') rcnt[i] += 1
            else lcnt[i] += 1
        }

        val ans = lar(n)
        for (i in 1 .. n) {
            var res = 0L
            if (str[i - 1] == '>') {
                res -= i
                if (lcnt[n] - lcnt[i] <= rcnt[i - 1]) {
                    // 出n+1
                    val cnt = lcnt[n] - lcnt[i]
                    val endpos = findFirst(1, i) { rcnt[i - 1] - rcnt[it] < cnt }
                    res += -(rpos[i - 1] - rpos[endpos - 1]) * 2
                    res += (lpos[n] - lpos[i]) * 2
                    res += n + 1
                } else {
                    // 出0
                    val cnt = rcnt[i - 1]
                    val endpos = findFirst(i, n) { lcnt[it] - lcnt[i] > cnt }
                    res += (lpos[endpos] - lpos[i]) * 2
                    res += -rpos[i - 1] * 2
                }
            } else {
                res += i
                if (rcnt[i - 1] <= lcnt[n] - lcnt[i]) {
                    // 出0
                    val cnt = rcnt[i - 1]
                    val endpos = findFirst(i, n) { lcnt[it] - lcnt[i] >= cnt }
                    res += (lpos[endpos] - lpos[i]) * 2
                    res += -rpos[i - 1] * 2
                } else {
                    // 出n+1
                    val cnt = lcnt[n] - lcnt[i]
                    val endpos = findFirst(1, i) { rcnt[i - 1] - rcnt[it] <= cnt }
                    res += -(rpos[i - 1] - rpos[endpos - 1]) * 2
                    res += (lpos[n] - lpos[i]) * 2
                    res += n + 1
                }
            }
            ans[i - 1] = res
        }

        for (res in ans) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}