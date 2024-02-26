package com.tauros.cp.archive.dp

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.findFirst
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/11
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1928/problem/E
    // n^1.5的做法
    // 关键点在于把背包dp部分转换成：达到指定个数的最小长度和
    // 然后枚举开头部分占用的长度，判断dp值是否小于等于剩余长度
    // 记录dp和pre的时候千万不要每次新申请数据，要利用原有数组，否则空间复杂度也变成n^1.5就会挂了
    val cases = rd.ni()
    repeat(cases) {
        val (n, x, y, s) = rd.na(4)

        val r = x % y
        val cutR = s - n.toLong() * r
        if (cutR < 0 || cutR % y != 0L) {
            wt.println("NO")
            return@repeat
        }

        val cnt = (cutR / y).toInt()
        val top = findFirst(cnt) { it * (it + 1L) / 2 > cnt } - 1
        val items = buildList {
            for (iter in 1 .. top) {
                add((iter.toLong() * (iter + 1) / 2).toInt() to iter + 1)
            }
        }

        val (dp, pre) = iar(cnt + 1) { INF } to ar(cnt + 1) { iao(-1, -1) }
        dp[0] = 0
        for ((sum, len) in items) {
            for (i in sum .. cnt) {
                if (dp[i - sum] + len < dp[i]) {
                    dp[i] = dp[i - sum] + len
                    pre[i][0] = i - sum
                    pre[i][1] = len
                }
            }
        }

        var (headCnt, headLen) = (x - r) / y to 1
        var curNum = headCnt
        while (headCnt <= cnt) {
            if (dp[cnt - headCnt] <= n - headLen) break
            headCnt += curNum + 1
            headLen += 1
            curNum += 1
        }
        if (headCnt > cnt) {
            wt.println("NO")
            return@repeat
        }

        val ans = iar(n) { r }
        ans[0] = x
        for (j in 1 until headLen) {
            ans[j] = ans[j - 1] + y
        }
        var iter = headLen
        var vol = cnt - headCnt
        while (vol > 0) {
            val len = pre[vol][1]
            for (j in 1 until len) ans[iter + j] = ans[iter + j - 1] + y
            iter += len
            vol = pre[vol][0]
        }

        wt.println("YES")
        for (res in ans) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}