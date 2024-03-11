package com.tauros.cp.archive.funny

import com.tauros.cp.bar

/**
 * @author tauros
 * 2024/3/11
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/576/B
    // 得手玩，挺难发现规律的
    // - 在perm上找环
    // - 如果有自环，也就是p[i] = i，那么其它所有点都往i上连就可以了
    // - 其它情况下，可以发现奇数环怎么玩也拼不起来
    // - 如果是个二元环，自己就可以连边
    // - 如果有多个二元环或者其它偶数环，可以发现，选择一个二元环 p0 - p1，其它偶数环 偶数下标连p0奇数下标连p1 就能把环展开
    // - 这种展开方式可以试下奇数环依然是展不成一颗树的
    // 于是答案就是当 有1元环或者有二元环且其它环大小都是偶数 时才有答案，构造方法如上
    val n = readln().toInt()
    val perm = readln().split(" ").map { it.toInt() - 1 }.toIntArray()

    val vis = bar(n)
    val loops = buildList {
        for (i in 0 until n) if (!vis[i]) {
            val loop = buildList {
                var iter = i
                do {
                    vis[iter] = true
                    add(iter)
                    iter = perm[iter]
                } while (iter != i)
            }
            add(loop)
        }
    }.groupBy { it.size }

    if (!(1 in loops || 2 in loops && loops.keys.all { it % 2 == 0 })) {
        println("NO")
        return
    }
    println("YES")
    print(buildString {
        if (1 in loops) {
            val root = loops[1]!![0][0]
            for ((_, cur) in loops) {
                for (loop in cur) for (u in loop) if (u != root) {
                    append("${root + 1} ${u + 1}\n")
                }
            }
        } else {
            val (p0, p1) = loops[2]!![0]
            append("${p0 + 1} ${p1 + 1}\n")
            for ((_, cur) in loops) for (loop in cur) {
                for (i in loop.indices step 2) {
                    val (u, v) = loop[i] to loop[i + 1]
                    if (u == p0 && v == p1) continue
                    append("${p0 + 1} ${u + 1}\n")
                    append("${p1 + 1} ${v + 1}\n")
                }
            }
        }
    })
}