package com.tauros.cp.archive.pain

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.iar
import kotlin.math.abs

/**
 * @author tauros
 * 2024/1/2
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/351/E
    // 需要智商：
    // 因为可正可负，不如全读进来非负数
    // 考虑最大的数，其对逆序对造成的影响不受其它数值的符号所影响
    // 1-indexed
    // 为负时：因为最小，所以前面的数无论什么符号都将与其产生逆序对，为 i - 1
    // 为正时：因为最大，所以后面的数无论什么符号都将与其产生逆序对，为 n - i
    // 那么最大的数贡献的逆序对为min(i - 1, n - i)
    // 由于其它的数对该最大的数产生的贡献都已经统计完了，这个数可以删掉
    // 继续迭代 第二大 ... 第N大
    //
    // 考虑值相同的数字，互相之间只有 排在后面的为负且前面的为正 时才会产生逆序对，否则逆序对为0
    // 设 pre[x] 为x位置之前比x小的数字数量，设 suf[x] 为x位置之后比x小的数字数量
    // 只有当 pre[x] - suf[x] < 0 时该位置才取负数
    // 设位置 i, j 其中 i < j，那么有 pre[i] <= pre[j] 和 suf[i] >= suf[j]
    // 可得 pre[i] - suf[i] <= pre[j] - suf[j] 因此不存在后面取了负数而前面却取正数的情况
    // 因此相同的数字可以一并处理，它们之间逆序对数为0
    val n = rd.ni()
    val nums = iar(n) { abs(rd.ni()) }

    val idx = iar(n) { it }
    val tmp = iar(n)
    val (llt, rlt) = iar(n) to iar(n)
    fun dac(st: int, ed: int) {
        if (ed - st <= 1) return
        val mid = st + ed shr 1
        dac(st, mid)
        dac(mid, ed)

        var (p, q) = st to mid
        while (p < mid) {
            while (q < ed && nums[idx[q]] < nums[idx[p]]) q++
            rlt[idx[p]] += q - mid
            p++
        }
        var (l, r) = st to mid
        while (r < ed) {
            while (l < mid && nums[idx[l]] < nums[idx[r]]) l++
            llt[idx[r]] += l - st
            r++
        }

        var iter = st
        var (x, y) = st to mid
        while (x < mid || y < ed) {
            tmp[iter++] = if (y >= ed || x < mid && nums[idx[x]] <= nums[idx[y]]) idx[x++] else idx[y++]
        }
        tmp.copyInto(idx, st, st, ed)
    }
    dac(0, n)

    val ans = (0 until n).sumOf { minOf(llt[it], rlt[it]) }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}