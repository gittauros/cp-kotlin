package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ao
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.graph.Graph
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/23
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/916/E
    // 分类讨论下
    // 真正的根为 0 一直不用动
    // 讨论当前 root 在更新和查询时，和 lca 的关系，分类做区间加减即可
    val (n, q) = rd.ni() to rd.ni()
    val vtx = rd.na(n)
    val graph = Graph(n, (n - 1) * 2)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
    }

    val (dfn, ori, size, dep, log) = ao(iar(n), iar(n), iar(n), iar(n), iar(n + 1))
    for (i in 2 .. n) log[i] = log[i / 2] + 1
    val st = ar(n) { iar(log[n] + 1) { -1 } }

    var idx = 0
    fun Graph.dfs(u: int, fa: int = -1, d: int = 0) {
        dfn[u] = idx++; ori[dfn[u]] = u; size[u] = 1; dep[u] = d; st[u][0] = fa
        for (b in 1 .. log[n]) st[u][b] = if (st[u][b - 1] == -1) break else st[st[u][b - 1]][b - 1]
        each(u) {
            val v = to[it]
            if (v == fa) return@each
            dfs(v, u, d + 1)
            size[u] += size[v]
        }
    }
    graph.dfs(0)

    infix fun int.up2Dep(d: int): int {
        var (len, v) = dep[this] - d to this
        while (len > 0) {
            v = st[v][len.countTrailingZeroBits()]
            len = len and len - 1
        }
        return v
    }
    fun lca(u: int, v: int): int {
        var (x, y) = iao(u, v).sortedBy { dep[it] }
        y = y up2Dep dep[x]
        if (x == y) return x
        for (b in log[n] downTo 0) if (st[x][b] != st[y][b]) {
            x = st[x][b]; y = st[y][b]
        }
        return st[x][0]
    }
    fun int.dfnRange() = dfn[this] until dfn[this] + size[this]
    infix fun int.inSub(u: int) = dfn[this] in u.dfnRange()

    class Seg(val cl: int, val cr: int) {
        val mid = cl + cr shr 1
        val len = cr - cl + 1
        var l: Seg? = null
        var r: Seg? = null
        var sum = 0L
        var lazy = 0L

        fun pushUp() {
            sum = (l?.sum ?: 0) + (r?.sum ?: 0)
        }

        fun build() {
            if (cl == cr) {
                sum = vtx[ori[cl]].toLong()
                return
            }
            l = Seg(cl, mid); r = Seg(mid + 1, cr)
            l?.build(); r?.build()
            pushUp()
        }

        fun accept(tag: long) {
            lazy += tag
            sum += len * tag
        }

        fun pushDown() {
            if (cl == cr) return
            if (lazy != 0L) {
                l?.accept(lazy); r?.accept(lazy)
                lazy = 0
            }
        }

        fun update(st: int, ed: int, add: int) {
            if (cl > ed || cr < st) return
            if (cl >= st && cr <= ed) {
                accept(add.toLong())
                return
            }
            pushDown()
            l?.update(st, ed, add); r?.update(st, ed, add)
            pushUp()
        }

        fun query(st: int, ed: int): long {
            if (cl > ed || cr < st) return 0
            if (cl >= st && cr <= ed) return sum
            pushDown()
            return l!!.query(st, ed) + r!!.query(st, ed)
        }
    }

    val seg = Seg(0, n - 1).apply { build() }
    var root = 0
    repeat(q) {
        val op = rd.ni()
        if (op == 1) {
            root = rd.ni() - 1
        } else if (op == 2) {
            val (u, v, x) = iao(rd.ni() - 1, rd.ni() - 1, rd.ni())
            val lca = lca(u, v)
            if (root inSub lca) {
                val (ur, vr) = lca(u, root) to lca(v, root)
                val joint = if (dep[ur] > dep[vr]) ur else vr
                seg.update(0, n - 1, x)
                if (root != joint) {
                    val upd = root up2Dep dep[joint] + 1
                    val range = upd.dfnRange()
                    seg.update(range.first, range.last, -x)
                }
            } else {
                val range = lca.dfnRange()
                seg.update(range.first, range.last, x)
            }
        } else {
            val u = rd.ni() - 1
            val ans = if (root inSub u) {
                var res = seg.sum
                if (root != u) {
                    val qry = root up2Dep dep[u] + 1
                    val range = qry.dfnRange()
                    val cut = seg.query(range.first, range.last)
                    res -= cut
                }
                res
            } else {
                val range = u.dfnRange()
                seg.query(range.first, range.last)
            }
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}