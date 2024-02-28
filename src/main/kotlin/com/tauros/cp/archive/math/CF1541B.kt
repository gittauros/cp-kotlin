package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2024/2/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1541/B
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

//        val sorted = nums.zip((1 .. n)).sortedBy { it.first }
//        var ans = 0L
//        for (i in 1 until n) {
//            val (num, p) = sorted[i]
//            for (j in 0 until i) {
//                val (pre, q) = sorted[j]
//                if (num * pre > 2 * n) break
//                if (num * pre == p + q) ans += 1
//            }
//        }
//        wt.println(ans)

        // i + j = a[i] * a[j]
        // j = a[i] * a[j] - i
        // 另 y=j, x=a[j]，相当于是求 k = a[i] b = -i 的直线 y = kx + b 和之前的点的交点
        // 每个点都是整点，斜率是 a[i] ，最小的 x 也是大于零的，所以这总个数是个调和级数求和的数量
        var ans = 0L
        for (i in 1 .. n) {
            val st = (i + nums[i - 1]) / nums[i - 1]
            var y = st * nums[i - 1] - i
            for (x in st .. 2 * n) {
                if (y >= i) break
                if (nums[y - 1] == x) ans += 1
                y += nums[i - 1]
            }
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}