package com.tauros.cp.archive.dp

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.structure.bitQuery
import com.tauros.cp.structure.bitUpdateWithIndex

/**
 * @author tauros
 * 2023/12/22
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1334/F
    // 补充前后哨兵，最前面补0，最后面补n+1
    val n = rd.ni() + 1
    val a = iao(0) + rd.na(n - 1) + iao(n)
    val p = iao(0) + rd.na(n - 1) + iao(0)
    val m = rd.ni() + 1
    val b = iao(0) + rd.na(m - 1) + iao(n)

    fun LongArray.update(pos: int, add: long) = bitUpdateWithIndex(pos) { this[it] += add }
    fun LongArray.query(pos: int) = bitQuery(pos, 0L, Long::plus)
    val (sum, neg) = lar(n + 1) to lar(n + 1)
    var total = 0L
    fun collect(a: int, p: long) {
        total += p
        sum.update(a, p)
        if (p < 0) neg.update(a, p)
    }

    // 从后往前统计
    // 当前a[i]对应的b[pos]的位置之前的一个位置称为pre
    // 统计包含i位置与之后 (大于a[i] 的所有数的cost的和)
    // 统计包含i位置与之后 (小于等于a[i] 的负数的的cost的和 减去 当前位置如果消耗为负的cost(因为当前位置一定大于pre位置，会被之后多减去))
    // 统计包含i位置与之后 (大于b[pre] 的所有数的cost的和)
    // 统计包含i位置与之后 (小于等于b[pre] 的负数的的cost的和)
    val pos = iar(n + 1) { -1 }
    for ((i, num) in b.withIndex()) pos[num] = i
    val props = ar(n + 1) { lar(4) }
    for (i in n downTo 1) {
        val cur = pos[a[i]]
        val pre = cur - 1
        collect(a[i], p[i].toLong())
        if (cur < 0) continue

        props[i][0] = neg.query(b[cur]) - if (p[i] < 0) p[i] else 0
        props[i][1] = total - sum.query(b[cur])
        props[i][2] = neg.query(b[pre])
        props[i][3] = total - sum.query(b[pre])
    }

    val g = lar(m + 1) { INF_LONG }
    // 大致转移如下，i为a数组位置，cur为对应b数组位置，pre为b数组在cur的前一个位置
    // f[i] = min { g[pre] } - le[i][pre] - gt[i][pre]
    // g[cur] = f[i] + le[i][cur] + gt[i][cur]
    // 当cur为m时的f[i]可以用于统计答案
    g[0] = total
    var ans = INF_LONG
    for (i in 1 .. n) {
        val cur = pos[a[i]]
        val pre = cur - 1
        if (pre < 0) continue

        val (leCur, gtCur, lePre, gtPre) = props[i]
        val dp = if (g[pre] >= INF_LONG) INF_LONG else g[pre] - lePre - gtPre
        if (cur == m) ans = minOf(ans, dp)

        g[cur] = minOf(g[cur], if (dp >= INF_LONG) INF_LONG else dp + leCur + gtCur)
    }

    if (ans >= INF_LONG) {
        wt.println("NO")
    } else {
        wt.println("YES")
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}

//fun solve2(n: int, a: IntArray, p: IntArray, m: int, b: IntArray): long {
//    val maxState = 1 shl n
//    var ans = INF_LONG
//    out@ for (state in 1 until maxState) {
//        var cost = 0L
//        val seq = buildList {
//            for (i in 0 until n) {
//                if (state shr i and 1 == 1) add(a[i + 1])
//                else cost += p[i + 1]
//            }
//        }
//        val res = buildList {
//            add(seq[0])
//            var pre = seq[0]
//            for (i in 1 .. seq.lastIndex) {
//                if (seq[i] > pre) {
//                    add(seq[i])
//                    pre = seq[i]
//                }
//            }
//        }
//        if (res.size != b.size - 1) continue
//        for (i in 0 until m) {
//            if (res[i] != b[i + 1]) continue@out
//        }
//        ans = minOf(ans, cost)
//    }
//    return if (ans >= INF_LONG) Long.MAX_VALUE else ans
//}