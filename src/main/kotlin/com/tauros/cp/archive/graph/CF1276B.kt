package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.DSU
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.iao

/**
 * @author tauros
 * 2024/1/17
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1276/B
    // oa a ab b ob
    // 去除a点，由b出发能到达的是ab + b + ob = x，那么oa = n - 1 - x
    // 同理求出ob，答案是oa * ob
    val cases = rd.ni()
    data class Edge(val u: int, val v: int)
    repeat(cases) {
        val (n, m, a, b) = iao(rd.ni(), rd.ni(), rd.ni() - 1, rd.ni() - 1)
        val edges = ar(m) { Edge(rd.ni() - 1, rd.ni() - 1) }
        fun calc(exp: int, other: int): long {
            val dsu = DSU(n)
            for ((u, v) in edges) if (u != exp && v != exp) {
                dsu.merge(u, v, false)
            }
            return n - 1L - dsu.size(other)
        }
        val expA = calc(a, b)
        val expB = calc(b, a)
        val ans = expA * expB
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}