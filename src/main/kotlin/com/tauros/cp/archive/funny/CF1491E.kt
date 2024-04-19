package com.tauros.cp.archive.funny

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.iao

/**
 * @author tauros
 * 2024/4/12
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1491/E
    // 斐波那契树判断，找到任意一条满足条件的边去分割然后再递归判断
    val n = rd.ni()
    val graph = Graph(n, (n - 1) * 2)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
    }

    val fibs = buildList {
        var (f1, f2) = 1 to 1
        add(1); add(1)
        while (f2 < n) {
            val f3 = f1 + f2
            add(f3)
            f1 = f2; f2 = f3
        }
    }
    if (n != fibs.last()) {
        wt.println("NO")
        return
    }

    val deleted = bar(graph.totalEdgeCnt)
    fun Graph.judge(root: int, idx: int): boolean {
        if (fibs[idx] == 1) return true
        // f[k]只能由f[k-1]+f[k-2]组成，因为小于k-1的两个斐波那契数相加一定小于f[k]
        // 因此至少有一个是f[k-1]，那么另一个就是f[k]-f[k-1]=f[k-2]
        val (smallCnt, bigCnt) = fibs[idx - 2] to fibs[idx - 1]
        // 只用找一个能切开成f[k-2]的边即可：
        // - 最多只有两个边满足切开后子树有f[k-2]，因为f[k-2]+f[k-3]+f[k-2]=f[k-1]+f[k-2]=f[k]
        // - 如果存在两条边，那么判断任意一条边切开后满足即可，因为另一棵f[k-2]一定会在f[k-1]的那一块再次被切开
        // - 不存在 一条边切开f[k-2]满足条件，另一条边切开f[k-2]不满足条件 的情况
        // - 因为只要满足一条边切开f[k-2]满足条件的话，另一边的f[k-2]会在f[k-1]的子树中被切开
        // - 此时要么整棵树都不满足条件，要么另一棵f[k-1]的也满足条件，也就是另一边的f[k-2]切开时也可以满足条件
        // 所以只需要选择任一能切出f[k-2]的边然后递归判断即可
        var (small, big) = -1 to -1
        fun dfs(u: int, fa: int): int {
            var size = 1
            each(u) {
                val v = to[it]
                if (v == fa || deleted[it]) return@each
                val subSize = dfs(v, u)
                if (small == -1 && big == -1 && subSize in iao(smallCnt, bigCnt)) {
                    if (subSize == smallCnt) {
                        small = v; big = u
                    } else {
                        small = u; big = v
                    }
                    deleted[it] = true; deleted[it xor 1] = true
                }
                size += subSize
            }
            return size
        }
        dfs(root, -1)
        return small != -1 && judge(small, idx - 2) && judge(big, idx - 1)
    }
    val ans = graph.judge(0, fibs.lastIndex)
    wt.println(if (ans) "YES" else "NO")
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}