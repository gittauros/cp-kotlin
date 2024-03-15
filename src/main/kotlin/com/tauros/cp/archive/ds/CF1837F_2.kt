package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1837/F
    // 关键就在于二分某个sum是否满足时，需要求前后缀子序列长度和不能超过k
    // 也就是分别求某个位置嘴边最小x个数的和与右边最小y个数的和都不超过sum时x+y>=k
    // 学习的这个写法：https://codeforces.com/contest/1837/submission/251331824
    // 维护双向链表实在是太麻烦很容易写错，这个写法非常好写也易于理解
    // 先求出每个数的rank，以及对应kth的每个下标
    // 遍历的过程中逐步加入数字，维护一个可以加入的数字的最大kth，大于这个kth的肯定不能加入
    // 删除时从维护的最大kth开始减少，判断这个kth是否已经被选择，如果被选择了再减少sum即可
    val cases = rd.ni()
    val cap = 1e9.toLong()
    repeat(cases) {
        val (n, k) = rd.ni() to rd.ni()
        val nums = rd.na(n)

        val kth = (0 until n).sortedBy { nums[it] }.toIntArray()
        val rank = iar(n)
        for (i in 0 until n) rank[kth[i]] = i

        val (chosen, len) = bar(n) to iar(n)
        var (sum, cnt) = 0L to 0
        var available = n
        val ans = findFirst(cap * k) { res ->
            fun offer(i: int) {
                if (rank[i] < available) {
                    sum += nums[i]; chosen[rank[i]] = true; cnt += 1
                }
                while (sum > res) {
                    available -= 1
                    if (chosen[available]) {
                        sum -= nums[kth[available]]; cnt -= 1
                    }
                }
            }
            sum = 0; cnt = 0; available = n; chosen.fill(false)
            for (i in n - 1 downTo 0) {
                offer(i)
                if (cnt >= k) return@findFirst true
                len[i] = cnt
            }
            sum = 0; cnt = 0; available = n; chosen.fill(false)
            for (i in 0 until n) {
                offer(i)
                if (cnt + (if (i == n - 1) 0 else len[i + 1]) >= k) return@findFirst true
            }
            false
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}