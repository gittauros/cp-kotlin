package com.tauros.cp.archive.mex

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.IIP
import com.tauros.cp.dq

/**
 * @author tauros
 * 2024/1/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1905/D
    // 考虑将第一个数移至末尾，设其值为a，会产生以下影响：
    // 1.第一个位置的mex值被去除
    // 2.小于等于a的mex值不受影响
    // 3.大于a的mex值变成a
    // 4.末尾接上mex=n
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        var res = 0L
        val vis = bar(n)
        val dq = dq<IIP>()
        var (iter, cnt) = 0 to 0
        for (num in nums) {
            vis[num] = true
            val pre = iter
            while (iter < n && vis[iter]) iter++
            if (iter != pre) {
                dq.addLast(pre to cnt)
                res += pre.toLong() * cnt
                cnt = 1
            } else {
                cnt++
            }
        }
        dq.addLast(iter to cnt)
        res += iter.toLong() * cnt

        var ans = res
        repeat(n - 1) {
            val (n0, c0) = dq.removeFirst()
            res -= n0
            if (c0 > 1) dq.addFirst(n0 to c0 - 1)

            cnt = 0
            while (dq.isNotEmpty() && dq.last().first > nums[it]) {
                val (n1, c1) = dq.removeLast()
                res -= n1.toLong() * c1
                cnt += c1
            }
            if (cnt > 0) {
                dq.addLast(nums[it] to cnt)
                res += nums[it].toLong() * cnt
            }

            dq.addLast(n to 1)
            res += n
            ans = maxOf(ans, res)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}