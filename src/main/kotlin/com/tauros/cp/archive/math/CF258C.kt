package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.ma
import com.tauros.cp.common.mm
import com.tauros.cp.common.ms
import com.tauros.cp.common.withMod
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.mmo
import com.tauros.cp.structure.IntList

/**
 * @author tauros
 * 2024/2/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/contest/258/problem/C
    // 一开始想容斥，想错了，推了一年，可以根据约数直接算
    // - 考虑枚举每个位置 lcm=max 的这个值，设为num，那么这个数组将由num的约数构成
    // - 统计可以取到某个值的位置个数，可以差分统计下
    // - 然后约数从大到小枚举，当前约数为div，能选div就一定能选比div小的约数
    // - 比如24的约数有 24 12 8 6 4 3 2 1，当前约数为24就有8个选项，当前约数为8就有6个选项，...
    // - 统计答案应该为 约数x选项个数^约数x位置数 * 约数y选项个数^约数y位置数 * ...
    // - 统计约数位置个数：前面已经选了sum个，那么当前能选min(n - sum, 能选当前约数位置个数 - sum)，从大到小执行...
    // - 然后考虑必须要有至少一个最大的约数，也就是其自身，把最大的约数单独拿出来逻辑处理下即可
    // - 瓶颈是筛约数，约数总个数规模为AlogA，快速幂还有个logA，所以是AlogAlogA
    val n = rd.ni()
    val nums = rd.na(n)

    val cap = 1e5.toInt()
    val cnt = iar(cap + 2)
    for (num in nums) {
        cnt[1] += 1
        cnt[num + 1] -= 1
    }
    for (num in 1..cap) cnt[num] += cnt[num - 1]

    withMod(1e9.toInt() + 7) {
        val ept = IntList(0)
        val divs = mmo<int, IntList>()
        var ans = 0
        for (i in 1..cap) if (cnt[i] > 0) {
            var (sum, less) = iao(cnt[i], 1)
            for (j in divs.getOrDefault(i, ept).indices.reversed()) {
                if (sum >= n) break
                val c = divs[i]!![j]
                val use = minOf(n - sum, c - sum)
                less = pow(j + 1L, use.toLong()) mm less
                sum += use
            }
            if (sum == n) {
                val divSize = divs.getOrDefault(i, ept).size.toLong()
                val topTotal = pow(divSize + 1, cnt[i].toLong())
                val topRemove = pow(divSize, cnt[i].toLong())
                val res = less mm (topTotal ms topRemove)
                ans = ans ma res
            }

            for (j in i..cap step i)
                divs.computeIfAbsent(j) { IntList() }.add(cnt[i])
        }
        wt.println(ans)
    }
}
fun main(args: Array<String>) {
    solve()
    wt.flush()
}