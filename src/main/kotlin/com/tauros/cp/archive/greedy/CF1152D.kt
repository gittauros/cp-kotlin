package com.tauros.cp.archive.greedy

import com.tauros.cp.ar
import com.tauros.cp.common.fill
import com.tauros.cp.common.mint
import com.tauros.cp.common.withMod
import com.tauros.cp.miar

/**
 * @author tauros
 * 2024/2/28
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1152/D
    // 不会做，很神奇，同深度且括号前缀和值相同的连出去的边肯定是一样的，子树肯定也是一样的，但是要取模不会比大小
    // 答案是贪心，先从叶子节点往上选边，一层一层隔着选，那么总答案就是深度为奇数点的个数，解释：
    // - 由于选的任何一条边一定是覆盖一个深度为奇数的点和一个深度为偶数的点，根节点深度为0
    // - 所以答案最多不会超过min(evenCnt, oddCnt)
    // - 由于叶子节点深度为偶数，那么evenCnt >= oddCnt是成立的，所以最多只能取到oddCnt，那么看能不能取到
    // - 考虑从叶子节点开始，每次都努力连它的父节点，那么所有深度为2n的节点可以把所有深度为2n-1的节点抢完，然后剥去下面两层，循环执行这个逻辑
    // - 上一步的逻辑要注意不是所有深度为2n的节点能被匹配，但是所有2n-1的节点都它被其中一个儿子抢走了
    // - 因此可以取满oddCnt，dp计算下oddCnt即可
    val n = readln().toInt()
    withMod(1e9.toInt() + 7) {
        val cnt = ar(2) { miar(n + 1) }
        cnt[0][0] = mint.ONE
        var cur = 0
        var ans = mint.ZERO
        for (len in 1 .. 2 * n) {
            val pre = cur; cur = 1 - cur
            cnt[cur].fill(0)
            for (top in 0 .. n) {
                val rest = 2 * n - len
                if (top > rest || (rest - top) % 2 == 1) continue
                if (top > 0) cnt[cur][top] += cnt[pre][top - 1]
                if (top < n) cnt[cur][top] += cnt[pre][top + 1]
                if (len % 2 == 1) {
                    ans += cnt[cur][top]
                }
            }
        }
        println(ans)
    }
}