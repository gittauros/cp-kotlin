package com.tauros.cp.archive.pain

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.iar
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2023/12/13
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
        val nums = rd.na(n)

        val ans = mlo<IIP>()
        val segs = mlo<int>()
        val q = dq<int>().apply { addAll(nums.toList()) }
        val ops = dq<int>()
        var ptr = 1
        while (q.isNotEmpty()) {
            val c = q.removeFirst()
            var first = q.indexOfFirst { it == c }
            if (first == -1) {
                wt.println(-1)
                return@repeat
            }
            while (first > 0) {
                ops.add(q.removeFirst())
                first -= 1
            }
            q.removeFirst()
            ptr += 1 + ops.size
            for ((i, num) in ops.withIndex()) {
                ans.add(ptr + i to num)
                q.addFirst(num)
            }
            segs.add(ops.size * 2 + 2)
            ptr += 1 + ops.size
            ops.clear()
        }

        wt.println(ans.size)
        for ((p, v) in ans) {
            wt.println("$p $v")
        }
        wt.println(segs.size)
        for (seg in segs) wt.print("$seg ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}