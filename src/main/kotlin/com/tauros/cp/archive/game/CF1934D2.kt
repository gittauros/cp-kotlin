package com.tauros.cp.archive.game

import com.tauros.cp.common.LLP
import com.tauros.cp.common.long
import com.tauros.cp.common.string
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/3/10
 */
fun main(args: Array<String>) {
    // https://codeforces.com/contest/1934/problem/D2
    // 考虑一个数n如何按这个规则拆分
    // 如果n是个2的幂，也就是只有一个设置位，那么无法拆分
    // 如果n有2个以上设置位，那么肯定可以拆分
    // 考虑下拆分方式，比如 11只能拆分为10和01，而110可以拆为101和011，也就是当已出现两个设置位时，后续的位置可以把0拆成两个1
    // 那么如果有偶数个设置位，一定可以拆分成两个奇数设置位的数，拆分方式：
    // - 先平均分，然后判断如果还是有偶数个设置位
    // - 看下最低位，如果相同，那么可以设置成两个1
    // - 如果不同，那么交换一下最低位
    // 奇数个设置位呢？要么平均分，平均分完后一定是一奇一偶，交换某一位还是一奇一偶，拆分0的话也还是一奇一偶
    // 只有拿到了偶数设置位的数，一定可以拆成两个奇数设置位的数，对方拆分后，再选择其中的偶数设置位的数即可
    // 随着位数降低，对方只会有2个只有一个设置位的数字可选，我们获胜
    val cases = readln().toInt()
    fun put(p1: long, p2: long) {
        println("$p1 $p2")
    }
    fun get(): LLP {
        val (p1, p2) = readln().split(" ").map(string::toLong)
        return p1 to p2
    }
    repeat(cases) {
        val n = readln().toLong()

        var (iter, turn) = n to false
        if (n.countOneBits() % 2 == 1) {
            println("second")
        } else {
            println("first")
            turn = true
        }
        while (true) {
            if (turn) {
                var (p, pick) = lar(2) to 0
                while (iter > 0) {
                    val low = iter.takeLowestOneBit()
                    p[pick] = p[pick] or low
                    pick = 1 - pick; iter -= low
                }
                if (p[0].countOneBits() % 2 == 0) {
                    val p00 = p[0] and 1L
                    val p10 = p[1] and 1L
                    if (p00 == p10) {
                        p[0] += 1L
                        p[1] += 1L
                    } else {
                        p[0] = p[0] xor 1
                        p[1] = p[1] xor 1
                    }
                }
                put(p[0], p[1])
            } else {
                val (p1, p2) = get()
                if (p1 == 0L && p2 == 0L) break
                iter = if (p1.countOneBits() % 2 == 0) p1 else p2
            }
            turn = !turn
        }
    }
}