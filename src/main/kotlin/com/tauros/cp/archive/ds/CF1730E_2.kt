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
    // 这个版本可以跑进两秒：
    // https://codeforces.com/problemset/submission/1730/244787732
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
            while (top >= 0 && nums[i] <= nums[stack[top]]) mnRight[stack[top--]] = i
            if (top >= 0) mnLeft[i] = stack[top]
            stack[++top] = i
        }

        var ans = 0L
        // min 在 max 左边
        val lpos = iar(cap + 1) { -1 }
        for (i in 0 until n) {
            val (mxl, mxr) = mxLeft[i] to mxRight[i]
            for (d in divs[nums[i]]!!) if (lpos[d] != -1) {
                val lp = lpos[d]
                if (mnRight[lp] > i && mxl < lp) {
                    ans += (lp - maxOf(mxl, mnLeft[lp])).toLong() * (minOf(mxr, mnRight[lp]) - i)
                }
            }
            lpos[nums[i]] = i
        }
        val rpos = iar(cap + 1) { n }
        // min 在 max 右边
        for (i in n - 1 downTo 0) {
            val (mxl, mxr) = mxLeft[i] to mxRight[i]
            for (d in divs[nums[i]]!!) if (rpos[d] != n) {
                val rp = rpos[d]
                if (mnLeft[rp] < i && mxr > rp) {
                    ans += (i - maxOf(mxl, mnLeft[rp])).toLong() * (minOf(mxr, mnRight[rp]) - rp)
                }
            }
            val last = rpos[nums[i]]
            if (last != n && mnLeft[last] < i) mnRight[i] = mnRight[last]
            rpos[nums[i]] = i
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