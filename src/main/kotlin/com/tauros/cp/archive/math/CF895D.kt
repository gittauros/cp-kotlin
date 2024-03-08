package com.tauros.cp.archive.math

import com.tauros.cp.common.ma
import com.tauros.cp.common.mm
import com.tauros.cp.common.withMod
import com.tauros.cp.iar
import com.tauros.cp.math.Comb

/**
 * @author tauros
 * 2024/3/8
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/895/D
    // 设从位置i开始的后缀总长度为len
    // 且从位置i开始的后缀字符x的总个数为cnt[x]
    // 那么位置i可选的字符集为{l, l+1, ..., r-1, r}的情况下的排列数为：
    // (len-1)! / (cnt[0]! * cnt[1]! * ... * cnt[25]!) * (cnt[l] + cnt[l+1] + ... + cnt[r])
    // 这个值里的 (len-1)! 和 1/(cnt[0]! * cnt[1]! * ... * cnt[25]!) 可以O(1)维护
    // 后者就是inv((cnt[0]! * cnt[1]! * ... * cnt[25]!)) = inv(cnt[0]!) * inv(cnt[1]!) * ... * inv(cnt[25]!)
    // 然后(cnt[l] + cnt[l+1] + ... + cnt[r])可以O(26)求
    // 接下来看怎么算合法排列，分为以下情况，从左往右考虑每个位置：
    // - a还未修改过，当前a[i]为第一个比b[i]小的位置，只考虑当前位置比b[i]小，产生贡献是 { a[i]+1, ..., b[i]-1 }
    // - a还未修改过，前面已经比b[i]小了，产生贡献是 { a[i]+1, ..., 25 }
    // - a已经修改了，前面和b的前缀完全相等，只考虑当前位置比b[i]小，产生贡献是 { 0, ..., b[i]-1 }
    // 可以看到，有两种状态，修改了a和未修改a
    // 对修改和未修改的分别维护前面提到的逆元累乘积和字符个数即可
    val a = readln().toCharArray()
    val b = readln().toCharArray()

    withMod(1e9.toInt() + 7) {
        val n = a.size
        val comb = Comb(n, globalMod)

        val cnt = iar(26)
        var perm = 1
        for (i in n - 1 downTo 0) {
            val c = a[i] - 'a'
            cnt[c] += 1
            perm = perm mm comb.inv[cnt[c]]
        }
        val eqCnt = cnt.clone()
        var eqPerm = perm

        var (notModifiedLess, modifiedPreEqGreat) = false to false
        var ans = 0
        for (i in 0 until n) {
            val (ac, bc) = a[i] - 'a' to b[i] - 'a'
            val len = n - i
            val part = comb.fac[len - 1] mm perm
            val eqPart = comb.fac[len - 1] mm eqPerm
            perm = perm mm cnt[ac]
            cnt[ac] -= 1
            if (!notModifiedLess && ac < bc) {
                val sum = (ac + 1 until bc).sumOf { cnt[it] }
                val res = part mm sum
                ans = ans ma res
                if (eqCnt[bc] > 0) {
                    modifiedPreEqGreat = true
                    eqPerm = eqPerm mm eqCnt[bc]
                    eqCnt[bc] -= 1
                } else {
                    eqPerm = eqPerm mm eqCnt[ac]
                    eqCnt[ac] -= 1
                }
                notModifiedLess = true
                continue
            }
            if (notModifiedLess) {
                val sum = (ac + 1 until 26).sumOf { cnt[it] }
                val res = part mm sum
                ans = ans ma res
            } else {
                eqPerm = eqPerm mm eqCnt[ac]
                eqCnt[ac] -= 1
            }
            if (modifiedPreEqGreat) {
                val sum = (0 until bc).sumOf { eqCnt[it] }
                val res = eqPart mm sum
                ans = ans ma res
                if (eqCnt[bc] > 0) {
                    eqPerm = eqPerm mm eqCnt[bc]
                    eqCnt[bc] -= 1
                } else {
                    modifiedPreEqGreat = false
                }
            }
        }
        println(ans)
    }
}