package com.tauros.cp.archive.greedy

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/19
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1943/C
    // 很难，不知道如何证明正确性
    // 考虑一条线的时候怎么操作，奇数个点次数肯定是 (n+1)/2 用中间的点距离从0到最大
    // - 偶数个点时，需要考虑 n%4 等于多少，因为偶数个点有两个中心，以两个中心可以把点集分隔开
    // - n%4=0时，n/2%2=0，所以分成两个集合后还是偶数个点，以 (center1,d), d=[0..n/4]; (center2,d), d=[0..n/4] 来操作即可，刚好可以覆盖
    // - n%4=2时，n/2%2=1，所以分成两个集合后是奇数个点，所以以两个中心来操作需要多操作一次满足那个额外的点
    // 回过头来看树，就得考虑树的直径，按树的直径的点数来操作，操作方法和单链一致
    // 首先肯定按这个操作方式，可以把树中最长的链搞定，也就是操作次数上界肯定不超过这个方法
    // 下界不会证，也许考虑方法就是要染色完直径最少也要这么多次操作？
    // 找直径中心用bfs挺方便的
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        if (n == 1) {
            wt.println(1)
            wt.println("1 0")
            return@repeat
        }
        val graph = Graph(n, (n - 1) * 2)
        val deg = iar(n)
        repeat(n - 1) {
            val (u, v) = rd.ni() - 1 to rd.ni() - 1
            graph.addEdge(u, v); graph.addEdge(v, u)
            deg[u] += 1; deg[v] += 1
        }

        val (len, center) = run {
            val inq = bar(n)
            val dq = dq<int>()
            var rest = n
            for (i in 0 until n) if (deg[i] == 1) {
                dq.addLast(i); inq[i] = true
                rest -= 1
            }
            var step = 0
            while (rest > 0) {
                repeat(dq.size) {
                    val u = dq.removeFirst()
                    graph.each(u) {
                        val v = graph.to[it]
                        if (inq[v]) return@each
                        if (--deg[v] == 1) {
                            dq.addLast(v); inq[v] = true
                            rest -= 1
                        }
                    }
                }
                step += 1
            }
            step to dq
        }

        if (center.size == 1) {
            val (c) = center
            wt.println(len + 1)
            for (d in 0 .. len)
                wt.println("${c + 1} $d")
        } else {
            val (c1, c2) = center
            wt.println((len + 2) / 2 * 2)
            for (d in 1 .. len + 1 step 2) {
                wt.println("${c1 + 1} $d")
                wt.println("${c2 + 1} $d")
            }
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}