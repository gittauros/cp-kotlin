package com.tauros.cp.archive.graph.shortestpath

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.graph.IGraph
import com.tauros.cp.lar
import com.tauros.cp.structure.LIHeap

/**
 * @author tauros
 * 2024/3/2
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1936/C
    // https://codeforces.com/blog/entry/126513
    // 完全不会，学了题解感觉是最短路优化dp的一种套路题之类的
    // - 可以推理得知每个宝可梦最多只上场一次，多次上场没有任何意义，因为既然能第二次上场击败某个宝可梦，那被击败的那个宝可梦都没必要上场
    // - 继续推理也可得知每个用于击败某只宝可梦的属性也只会使用一次，因为击败了后胜者已经带着这个属性上场了，被这个属性再次击败也不会上场，无意义
    // - 那么属性提升的操作只会提升到刚好等于某个属性的值
    // - 原问题可以转换为第n号宝可梦站在场上最终成功击败1号宝可梦，以n号宝可梦为起始点建图
    // - 每个属性拆为两个点，一个实值点A，一个降值通道点D，这样建图
    // - 同一个属性内：实值点 -> 降值点，边权为0；意为不用费用即可使用更低属性值的属性
    // - 点i -> 点i的实值点，边权为cost[i]；意为需要需要通过点i使用这个属性的实值，这条边是用来从点i的属性j转到点i某个属性k时使用的
    //   相当于表示购买了这个宝可梦，之所以为什么点i对它的每个实值属性都要花费cost[i]
    //   其实从前面的推理可以得知某个宝可梦的只会使用一次某个属性，所以这样建图才成立
    // - 点i的降值点 -> 点i，边权为0；意为从降值通道走到了某个宝可梦，相当于是用高值属性击败了这个宝可梦
    //   如果要使用该宝可梦的另一个属性，那么才需要实际执行cost，走上面那条边即可
    // - 每项属性按属性值排序，相邻的属性间：
    //   实值点 -> 相邻的更高实值点，边权为差值；意为花钱升级属性值
    //   降值点 -> 相邻的更低降值点，边权为0；意为不用钱就能使用任意低于其值的任何一个降值点
    //   通过降值点走到某个宝可梦来执行其它属性
    // 非常好题目，使我学了很多
    val cases = rd.ni()
    repeat(cases) {
        val (n, m) = rd.ni() to rd.ni()
        val cost = rd.na(n)
        val attr = ar(n) { rd.na(m) }

        fun actualAttrIdx(i: int, j: int) = i * m + j
        fun goDownAttrIdx(i: int, j: int) = n * m + i * m + j
        fun vtxIdx(i: int) = n * m * 2 + i
        val graph = IGraph(n * m * 2 + n)

        for (j in 0 until m) {
            val sorted = (0 until n).sortedBy { attr[it][j] }
            for (k in 0 until n) {
                val cur = sorted[k]
                if (k + 1 < n) {
                    val next = sorted[k + 1]
                    graph.addEdge(actualAttrIdx(cur, j), actualAttrIdx(next, j), attr[next][j] - attr[cur][j])
                    graph.addEdge(goDownAttrIdx(next, j), goDownAttrIdx(cur, j), 0)
                }
                graph.addEdge(actualAttrIdx(cur, j), goDownAttrIdx(cur, j), 0)
            }
            for (i in 0 until n) {
                graph.addEdge(vtxIdx(i), actualAttrIdx(i, j), cost[i])
                graph.addEdge(goDownAttrIdx(i, j), vtxIdx(i), 0)
            }
        }

        val dist = lar(n * m * 2 + n) { 0x3f3f3f3f3f3f3f3fL }
        val vis = bar(n * m * 2 + n)
        val heap = LIHeap()
        heap.offer(0L to vtxIdx(n - 1))
        dist[vtxIdx(n - 1)] = 0
        while (heap.isNotEmpty()) {
            val (curDist, u) = heap.poll()
            if (vis[u]) continue
            vis[u] = true
            graph.each(u) {
                val (v, w) = graph.to[it] to graph.weight[it]
                if (curDist + w < dist[v]) {
                    dist[v] = curDist + w
                    heap.offer(dist[v] to v)
                }
            }
        }

        val ans = dist[vtxIdx(0)]
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}