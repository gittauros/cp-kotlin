package com.tauros.cp.archive.greedy

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/9
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1231/E
    // 一开始想区间dp，想了半天状态都找不到一个
    // 操作允许完成任意排列，所以只要字符个数都对上就能转换过去
    // 试想通过往前缀后缀放字符的方式，最多只要n次一定可以排成指定字符（相当于选择排序，最多选n次）
    // 那么考虑最优方案下，被选择到后面和前面的，一定是target字符的某个后缀和前缀
    // 那么不属于后缀和前缀的就是不动的部分，这一部分越长，操作数就越小
    // 也就是求中间连续段最长，考虑中间连续段需要满足什么
    // 中间连续段只需要满足能和source的某个子序列匹配上就好了
    // 所以问题就转换成了从target的i出发的连续子段，匹配source的子序列，最长能匹配多少，n^2就能解决
    val cap = 26
    val q = rd.ni()
    repeat(q) {
        val n = rd.ni()
        val (s, t) = rd.ns(n) to rd.ns(n)

        val cnt = iar(cap)
        for (c in t) cnt[c - 'a'] += 1
        for (c in s) cnt[c - 'a'] -= 1
        if (cnt.any { it != 0 }) {
            wt.println(-1)
            return@repeat
        }
        var ans = 0
        for (st in 0 until n) {
            var ti = st
            for (si in 0 until n) if (ti < n) {
                if (s[si] == t[ti]) ti += 1
            }
            ans = maxOf(ti - st, ans)
        }
        wt.println(n - ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}