package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.MInt
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.common.toMInt
import com.tauros.cp.common.withMod
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.structure.bitQuery
import com.tauros.cp.structure.bitUpdateWithIndex
import kotlin.math.abs

/**
 * @author tauros
 * 2023/12/25
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1917/problem/D
    // 数组按行列来看长这样
    // p[0] * 2^q[0], p[0] * 2^q[1], ... , p[0] * 2^q[k-1]
    // p[1] * 2^q[0], p[1] * 2^q[1], ... , p[1] * 2^q[k-1]
    // p[2] * 2^q[0], p[2] * 2^q[1], ... , p[2] * 2^q[k-1]
    // 每一行为一组，组内p值相等，逆序对等于q数组的逆序对
    // 有n组: inner = n * inverses(q)
    //
    // 考虑组间的逆序对，p[i] 会对所有 p[i+]产生贡献，满足条件为: p[i] * 2^q[j] > p[i+] * 2^q[all]
    // 变形后为: 2^q[j] / 2^q[all] > p[i+] / p[i] ①
    // 不好把p或者q完全分离看待，对这个式子左右分别处理
    // 左侧:
    // 左侧对不等式右侧来说是一模一样的，其意义为，每组间的指数比值
    // 由于q的值域为 [0,k-1]，所以这个左侧是个固定值:
    // -(k - 1), ..  -1, 0
    // -(k - 2), ..   0, 1
    // ...
    // 0, ..  k - 2, k - 1
    // 累加起来长这样：
    // -(k-1), -(k-2), ...  -1, 0,   1, ... k-2, k-1
    //      1,      2, ... k-1, k, k-1, ...   2,   1
    // 左右添加0的哨兵后长度为2k+1
    // 右侧:
    // p[i+] / p[i] 既可以p数组从后往前处理也可以从前往后处理
    // 从前往后看就是，当前的 p[i] 对之前的p值的比值，查询大于这些比值的而的指数总数
    // 由于p的值域也固定了 [1,2n-1]
    // 所以，对每个p[i]枚举乘以2的指数为-1 -2 -3和1 2 3，查询大于等于该指数时可以满足不等式①的p的取值范围，比如:
    // 1.对于 p[i]=5 查询2的指数在区间 (-1, 0] 时满足不等式的p取值范围为: [ 6, 10] -> [(5*2^0) + 1, 5*2^1]
    //   这部分满足的q的范围为 [ 0, k-1]
    // 2.对于 p[i]=5 查询2的指数在区间 (-2, -1] 时满足不等式的p取值范围为: [11, 20] -> [(5*2^1) + 1, 5*2^2]
    //   这部分满足的q的范围为 [ -1, k-1]
    // 3.对于 p[i]=5 查询2的指数在区间 ( 0, 1] 时满足不等式的p取值范围为: [ 3, 5] -> [(5*2^-1) + 1, 5*2^0]
    //   这部分满足的q的范围为 [ 1, k-1]
    // 4.对于 p[i]=5 查询2的指数在区间 ( 1, 2] 时满足不等式的p取值范围为: [ 2, 2] -> [(5*2^-2) + 1, 5*2^-1]
    //   这部分满足的q的范围为 [ 2, k-1]
    // 左侧q的数据是固定的，右侧p用树状数组维护前缀和即可
    // 组间答案为: cross
    //
    // 总答案为: inner + cross
    // 总复杂度 nlogn
    val cases = rd.ni()
    repeat(cases) {
        val (n, k) = rd.ni() to rd.ni()
        val p = rd.na(n)
        val q = rd.na(k)

        withMod(998244353) {
            fun IntArray.query(pos: int) = this.bitQuery(pos, 0, Int::plus)
            fun IntArray.update(pos: int) = this.bitUpdateWithIndex(pos) { this[it] += 1 }

            val innerPowBit = iar(k + 1)
            var inner = MInt.ZERO
            for (i in k - 1 downTo 0) {
                inner += innerPowBit.query(q[i])
                innerPowBit.update(q[i] + 1)
            }
            inner *= n

            val bits = lar(2 * k + 1) { k.toLong() - abs(it - k) }
            val sumBits = bits.runningReduce(Long::plus)
            val totalPow = sumBits.last().toMInt()
            fun queryPowLe(pow: int): long {
                val query = minOf(sumBits.lastIndex, maxOf(0, pow + k))
                return sumBits[query]
            }

            val crossNumBit = iar(2 * n)
            var cross = MInt.ZERO
            for (i in 0 until n) {
                // ge
                var geCnt = MInt.ZERO
                for (bit in 0 downTo -20) {
                    val (cl, cr) = (p[i] shl -bit) + 1 to minOf(p[i] shl -bit + 1, 2 * n - 1)
                    if (cl >= 2 * n) break
                    val pCnt = totalPow - queryPowLe(bit - 1)
                    val qCnt = crossNumBit.query(cr) - crossNumBit.query(cl - 1)
                    geCnt += pCnt * qCnt
                }
                // lt
                var ltCnt = MInt.ZERO
                for (bit in 1 .. 20) {
                    val (cl, cr) = (p[i] shr bit) + 1 to (p[i] shr bit - 1)
                    if (cr <= 0) break
                    val pCnt = totalPow - queryPowLe(bit - 1)
                    val qCnt = crossNumBit.query(cr) - crossNumBit.query(cl - 1)
                    ltCnt += pCnt * qCnt
                }
                cross += geCnt + ltCnt
                crossNumBit.update(p[i])
            }
            val ans = inner + cross
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}