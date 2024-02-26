package com.tauros.cp.archive.funny

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.swap
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/21
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1918/problem/E
    // 快排
    fun ask(i: int): int {
        wt.println("? $i")
        wt.flush()
        val c = rd.nc()
        return if (c == '<') -1 else if (c == '>') 1 else 0
    }
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()

        val idxes = iar(n) { it }
        idxes.shuffle()

        fun sort(st: int, ed: int) {
            if (st >= ed - 1) return
            val pick = st + ed shr 1
            if (pick != st) idxes.swap(st, pick)

            while (ask(idxes[st] + 1) != 0);
            var (p, q) = st to ed - 1
            while (p != q) {
                val v = ask(idxes[q] + 1); ask(idxes[p] + 1)
                if (q < p) {
                    if (v < 0) q += 1
                    else {
                        idxes.swap(p, q)
                        val tmp = p; p = q; q = tmp
                        q -= 1
                    }
                } else {
                    if (v > 0) q -= 1
                    else {
                        idxes.swap(p, q)
                        val tmp = p; p = q; q = tmp
                        q += 1
                    }
                }
            }

            sort(st, p); sort(p + 1, ed)
        }
        sort(0, n)

//        fun sort(list: List<int>): List<int> {
//            if (list.size <= 1) return list
//            val range = list.shuffled()
//
//            while (ask(range[0] + 1) != 0);
//            val (left, right) = mlo<int>() to mlo<int>()
//            for (i in 1 until range.size) {
//                if (ask(range[i] + 1) < 0) left.add(range[i])
//                else right.add(range[i])
//                ask(range[0] + 1)
//            }
//
//            return sort(left) + lo(range[0]) + sort(right)
//        }
//        val idxes = sort((0 until n).toList())

        val ans = iar(n)
        for ((num, i) in idxes.withIndex()) ans[i] = num + 1

        wt.print("! ")
        for (res in ans) wt.print("$res ")
        wt.println()
        wt.flush()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}