package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.reverse
import kotlin.math.abs

/**
 * @author tauros
 * 2024/2/7
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1558/C
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        var success = true
        val ans = buildList {
            for (i in n - 1 downTo 2 step 2) {
                val (o, e) = i + 1 to i
                val (oIdx, eIdx) = nums.indexOf(o) to nums.indexOf(e)
                if (oIdx % 2 == 1 || eIdx % 2 == 0) {
                    success = false
                    return@buildList
                }
                if (oIdx == eIdx + 1 && oIdx == i) continue
                if (abs(oIdx - eIdx) > 1) {
                    if (oIdx != 0) {
                        add(oIdx + 1)
                        nums.reverse(0, oIdx + 1)
                    }
                    if (oIdx < eIdx) {
                        add(eIdx)
                        nums.reverse(0, eIdx)
                    } else {
                        add(oIdx - eIdx)
                        nums.reverse(0, oIdx - eIdx)
                    }
                }
                val (oAdj, eAdj) = nums.indexOf(o) to nums.indexOf(e)
                if (oAdj < eAdj) {
                    if (oAdj != 0) {
                        add(eAdj + 2)
                        nums.reverse(0, eAdj + 2)
                        add(eAdj + 1 - oAdj + 1)
                        nums.reverse(0, eAdj + 1 - oAdj + 1)
                    }
                } else {
                    if (oAdj == i) continue
                    add(oAdj + 1)
                    nums.reverse(0, oAdj + 1)
                }
                add(i + 1)
                nums.reverse(0, i + 1)
            }
        }
        if (!success) {
            wt.println(-1)
        } else {
            wt.println(ans.size)
            if (ans.isNotEmpty()) {
                for (res in ans) wt.print("$res ")
                wt.println()
            }
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}