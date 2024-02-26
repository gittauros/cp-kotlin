package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/1/16
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val str = rd.ns(n)
        val lb = str.count { it == '(' }
        if (lb + lb != n) {
            wt.println(-1)
            return@repeat
        }

        val stack = iar(n)
        val ans = iar(n)
        fun Iterable<IndexedValue<Char>>.deal(): int {
            var cnt = 0
            var top = -1
            ans.fill(2)
            for ((i, c) in this) {
                if (c == '(') {
                    stack[++top] = i
                } else if (top >= 0) {
                    ans[stack[top--]] = 1
                    ans[i] = 1
                    cnt += 2
                }
            }
            return cnt
        }
        fun out(cnt: int) {
            wt.println(if (cnt == n || cnt == 0) 1 else 2)
            for (res in ans) wt.print("${if (cnt == 0) res - 1 else res} ")
            wt.println()
        }
        val c0 = str.withIndex().deal()
        if (c0 == n || c0 == 0) {
            out(c0)
            return@repeat
        }
        val c1 = str.withIndex().reversed().deal()
        out(c1)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}