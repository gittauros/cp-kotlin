package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar
import com.tauros.cp.math.Prime

/**
 * @author tauros
 * 2024/2/2
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private val cap = 1e6.toInt()
private val primes = Prime(cap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1730/E
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)
        val set = nums.toSet()
        val divs = buildMap { for (num in set) put(num, primes.factors(num).filter { it in set }.toIntArray()) }

        var top = -1
        val stack = iar(n)
        val (mxLeft, mxRight) = iar(n) { -1 } to iar(n) { n }
        for (i in 0 until n) {
            while (top >= 0 && nums[i] > nums[stack[top]]) mxRight[stack[top--]] = i
            if (top >= 0) mxLeft[i] = stack[top]
            stack[++top] = i
        }
        top = -1
        val (mnLeft, mnRight) = iar(n) { -1 } to iar(n) { n }
        for (i in 0 until n) {
            while (top >= 0 && nums[i] < nums[stack[top]]) mnRight[stack[top--]] = i
            stack[++top] = i
        }
        top = -1
        for (i in 0 until n) {
            while (top >= 0 && nums[i] <= nums[stack[top]]) stack[top--]
            if (top >= 0) mnLeft[i] = stack[top]
            stack[++top] = i
        }

        // 预处理右侧某值的位置，可以直接拿右侧约数
        val (next, rpos) = iar(n) to iar(cap + 1) { n }
        for (i in n - 1 downTo 0) {
            next[i] = rpos[nums[i]]
            rpos[nums[i]] = i
        }
        // 维护左边某值的位置，可以直接拿左侧约数
        val lpos = iar(cap + 1) { -1 }
        var ans = 0L
        for ((i, num) in nums.withIndex()) {
            rpos[num] = next[i]
            // 容斥，过不去
//            val (mxl, mxr) = mxLeft[i] to mxRight[i]
//            for (div in divs[num]!!) if (lpos[div] != -1 || rpos[div] != n) {
//                val (lp, rp) = lpos[div] to rpos[div]
//                if (lp != -1 && mxl < lp && mnRight[lp] > i) {
//                    ans += (lp.toLong() - maxOf(mxl, mnLeft[lp])) * (minOf(mxr, mnRight[lp]).toLong() - i)
//                }
//                if (rp != n && mnLeft[rp] < i && mxr > rp) {
//                    ans += (i.toLong() - maxOf(mxl, mnLeft[rp])) * (minOf(mxr, mnRight[rp]).toLong() - rp)
//                }
//                if (lp != -1 && rp != n) {
//                    val (cl, cr) = maxOf(mxl, mnLeft[lp], mnLeft[rp]) to minOf(mxr, mnRight[lp], mnRight[rp])
//                    if (cl < lp && cr > rp) ans -= (lp.toLong() - cl) * (cr.toLong() - rp)
//                }
//            }
            // 修改最大值区间左端点，不用容斥，可以过
            val mxr = mxRight[i]
            for (div in divs[num]!!) if (lpos[div] != -1 || rpos[div] != n) {
                var mxl = mxLeft[i]
                val (lp, rp) = lpos[div] to rpos[div]
                // min 在 max 左侧
                if (lp != -1 && mxl < lp && mnRight[lp] > i) {
                    ans += (lp.toLong() - maxOf(mxl, mnLeft[lp])) * (minOf(mxr, mnRight[lp]).toLong() - i)
                    mxl = maxOf(lp, mxl)
                }
                // min 在 max 右侧
                if (rp != n && mnLeft[rp] < i && mxr > rp) {
                    ans += (i.toLong() - maxOf(mxl, mnLeft[rp])) * (minOf(mxr, mnRight[rp]).toLong() - rp)
                }
            }
            lpos[num] = i
        }
        // max 和 min 同一个位置
        ans += n

        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}