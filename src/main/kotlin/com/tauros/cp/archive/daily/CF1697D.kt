package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.car
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.common.string
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2024/2/19
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()

    fun askChar(i: int): Char {
        wt.println("? 1 $i")
        wt.flush()
        return rd.nc()
    }
    fun askDiff(l: int, r: int): int {
        wt.println("? 2 $l $r")
        wt.flush()
        return rd.ni()
    }
    fun out(s: string) = wt.println("! $s")

    val starts = buildList {
        var iter = 1
        add(1)
        while (iter < n) {
            val diff = askDiff(iter, iter + 1)
            if (diff == 2) add(iter + 1)
            iter += 1
        }
    }.toIntArray()
    val ends = buildList {
        for (i in 0 until starts.lastIndex) add(starts[i + 1] - 1)
        add(n)
    }.toIntArray()

    val ans = car(n)
    val chs = car(starts.size)
    val posList = mlo<int>()
    chs[0] = askChar(ends[0])
    posList.add(ends[0])
    ans.fill(chs[0], starts[0] - 1, ends[0])
    if (chs.size > 1) {
        chs[1] = askChar(ends[1])
        posList.add(ends[1])
        ans.fill(chs[1], starts[1] - 1, ends[1])
    }
    for (i in 2 until ends.size) {
        posList.sort()
        val f = findFirst(posList.size) {
            val (st, ed) = posList[posList.lastIndex - it] to ends[i]
            val diff = askDiff(st, ed)
            diff < it + 2
        }
        if (f < posList.size) {
            val idx = posList[posList.lastIndex - f]
            chs[i] = ans[idx - 1]
            posList.remove(idx)
            posList.add(ends[i])
        } else {
            chs[i] = askChar(ends[i])
            posList.add(ends[i])
        }
        ans.fill(chs[i], starts[i] - 1, ends[i])
    }
    out(String(ans))
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}