package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/6
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val nums = rd.na(n)
    val (inc, dec) = iar(n) to iar(n)

    var (incTop, decTop) = -1 to -1
    for ((i, num) in nums.withIndex()) {
        val (incBound, decBound) = (if (incTop == -1) -INF else nums[inc[incTop]]) to (if (decTop == -1) INF else nums[dec[decTop]])
        if (num in incBound + 1 until decBound) {
            if (num > incBound) inc[++incTop] = i
            else dec[++decTop] = i
            continue
        }
        if (incBound >= decBound) {
            if (num > incBound) {
                inc[++incTop] = i
            } else if (num < decBound) {
                dec[++decTop] = i
            } else {
                wt.println("NO")
                return
            }
        } else {
            if (num > decBound) {
                if (decTop >= 0 && (incTop == -1 || dec[decTop] > inc[incTop])) {
                    inc[++incTop] = dec[decTop--]
                }
                inc[++incTop] = i
            } else if (num < incBound) {
                if (incTop >= 0 && (decTop == -1 || inc[incTop] > dec[decTop])) {
                    dec[++decTop] = inc[incTop--]
                }
                dec[++decTop] = i
            } else if (num == decBound) {
                inc[++incTop] = i
            } else {
                dec[++decTop] = i
            }
        }
    }

    wt.println("YES")
    val ans = iar(n)
    while (decTop >= 0) ans[dec[decTop--]] = 1
    for (res in ans) wt.print("$res ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}