package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.mlo
import com.tauros.cp.structure.bitQuery
import com.tauros.cp.structure.bitUpdateWithIndex

/**
 * @author tauros
 * 2024/2/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/383/C
    // 题意是 更新全部子树的节点的值 和 查询某个节点的值，更新的方式是 u节点+val, u儿子节点-val, u孙子节点+val ...
    // 做法一：按深度奇偶分成两份dfn序列，奇偶性相同为加，不同为减，更新时区间内离散化一下
    // 做法二：规定深度偶数为加，深度奇数为减，那么u节点深度为偶数时，就整个u区间都加反之全减；查询时深度为偶数的节点直接加，奇数节点取相反数
    // 做法二少一个线段数组，且不用离散化，会快很多
    val (n, m) = rd.ni() to rd.ni()
    val vtx = rd.na(n)
    val graph = ar(n) { mlo<int>() }
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph[u].add(v); graph[v].add(u)
    }

    val (dep, st, ed) = ar(3) { iar(n) }
    fun dfs(u: int, fa: int = -1, d: int = 0, dfn: int = 1): int {
        dep[u] = d; st[u] = dfn; ed[u] = dfn + 1
        for (v in graph[u]) if (v != fa) {
            ed[u] = dfs(v, u, d + 1, ed[u])
        }
        return ed[u]
    }
    dfs(0)

    fun IntArray.update(pos: int, add: int) = this.bitUpdateWithIndex(pos) { this[it] += add }
    fun IntArray.query(pos: int) = this.bitQuery(pos, 0, int::plus)

    val bit = iar(n + 2)
    repeat(m) {
        val (op, x) = rd.ni() to rd.ni() - 1
        val c = if (dep[x] % 2 == 0) 1 else -1
        if (op == 1) {
            val v = rd.ni()
            val (cl, cr) = st[x] to ed[x]
            bit.update(cl, v * c)
            bit.update(cr, -v * c)
        } else {
            val add = bit.query(st[x])
            val ans = vtx[x] + c * add
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}