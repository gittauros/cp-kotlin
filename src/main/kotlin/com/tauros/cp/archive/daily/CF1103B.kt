package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.IIP
import com.tauros.cp.common.boolean
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int

/**
 * @author tauros
 * 2024/1/23
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1103/B
    fun cmd(): boolean {
        val str = rd.ns()
        return str == "start"
    }
    fun ask(x: int, y: int): int {
        wt.println("? $x $y")
        wt.flush()
        val c = rd.nc()
        return if (c == 'x') 0 else if (c == 'y') 1 else -1
    }
    fun out(a: int) {
        wt.println("! $a")
        wt.flush()
    }
    fun findRange(): IIP? {
        var (st, len) = 1 to 1
        while (true) {
            val ed = st + len
            val res = ask(st, ed)
            if (res == -1) return null
            if (res == 0) return st to ed
            len *= 2
            st = ed
        }
    }
    while (cmd()) {
        val (st, ed) = findRange() ?: break
        val a = findFirst(st + 1, ed) {
            val res = ask(st, it)
            if (res == -1) return
            res == 0
        }
        if (ask(a, st) == 0) out(a - st)
        else out(a)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}