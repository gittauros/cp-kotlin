package com.tauros.cp.archive.daily

import com.tauros.cp.common.nextPermutation
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/20
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/375/A
    // 1689的排列模7结果从0到6都有
    // 把除1689外的放前面，看下余数多少，爆搜乱拼即可
    val str = readln()

    val cnt = iar(10)
    for (c in str) cnt[c - '0'] += 1

    val special = iao(1, 6, 8, 9)
    val ans = buildString {
        var mod = 0
        for (i in 9 downTo 0) {
            while (cnt[i] > if (i in special) 1 else 0) {
                append(i); cnt[i] -= 1
                mod = (mod * 10 + i) % 7
            }
        }
        if (isBlank() || startsWith('0')) {
            insert(0, "1869")
        } else {
            mod = mod * 10000 % 7
            do {
                var tail = 0
                for (num in special) {
                    tail = (tail * 10 + num) % 7
                }
                if ((tail + mod) % 7 == 0) {
                    special.onEach { append(it) }
                    break
                }
            } while (special.nextPermutation())
        }
    }
    println(ans)
}