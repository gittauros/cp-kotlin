package com.tauros.cp.archive.dp

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.ma
import com.tauros.cp.common.ms
import com.tauros.cp.common.withMod
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/25
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1943/D1
    // https://codeforces.com/blog/entry/127195
    // 这个做法和官方题解不一样
    // 考虑最后一个数字能够被削减到的区间，假设为[l, r]
    // 每次添加一个数后：
    // - 如果num >= r，那么新的范围被更新为[num-r, num]
    // - 如果num >= l && num < r，那么新的范围被更新为[0, num]
    // - 如果num < l，无解
    // - 处理完最后一个数范围最小值必须是0
    // 那么状态是dp[l][r]，起始值是dp[0][0]=1，看如何转移
    // 如果枚举每个数num如何填，还需要枚举l和r，那么复杂度会是n^4，需要优化一下
    // 将转移式改为：
    // - num >= l && num < r && l <= r时，dp[0][num] = sum { dp[l][r] }，相当于每个[l, r]对区间内的num都有贡献，做个差分前缀和来更新
    // - num >= r && l <= r时，dp[num-r][num] = sum { dp[l][r] }，相当于每个[l, r]对所有dp[num-r+j][num+j]都有贡献，相当于是个斜着的差分前缀和
    // 这样就优化到n^3了，答案是sum { dp[0][r] }
    //
    // 官解给出的条件是a[i-1]+a[i+1]>=a[i]即可，这个条件也是很好dp的，视a[-1]和a[n]是0即可
    // 我做法的条件转换一下也可以推出这个关系，但是很不直观，不会证明充分性
    val cases = rd.ni()
    repeat(cases) {
        val (n, k, p) = rd.na(3)
        withMod(p) {
            val dp = ar(2) { ar(k + 1) { iar(k + 1) } }
            val diff = ar(k + 1) { iar(k + 1) }
            dp[0][0][0] = 1
            var cur = 0
            repeat(n) {
                val pre = cur; cur = 1 - cur
                dp[cur].onEach { it.fill(0) }
                diff.onEach { it.fill(0) }

                for (min in 0 .. k) for (max in min .. k) {
                    dp[cur][0][min] = dp[cur][0][min] ma dp[pre][min][max]
                    dp[cur][0][max] = dp[cur][0][max] ms dp[pre][min][max]

                    diff[0][max] = diff[0][max] ma dp[pre][min][max]
                }
                for (i in 1 .. k) dp[cur][0][i] = dp[cur][0][i] ma dp[cur][0][i - 1]

                for (min in 1 .. k) for (max in min .. k) diff[min][max] = diff[min][max] ma diff[min - 1][max - 1]
                for (min in 0 .. k) for (max in min .. k) {
                    dp[cur][min][max] = dp[cur][min][max] ma diff[min][max]
                }
            }
            val ans = dp[cur][0].fold(0, int::ma)
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}