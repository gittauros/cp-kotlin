package com.tauros.cp.archive.funny

import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/4/24
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/765/D
    // g(h(x)) = x
    // h(g(x)) = f(x)
    // -> h(x) = h(g(h(x))) = f(h(x))
    // -> h(g(x)) = f(h(g(x))) = f(f(x)) = f(x)
    // 所以得满足 f(f(x)) = f(x)
    val n = readln().toInt()
    val f = readln().split(" ").map { it.toInt() - 1 }.toIntArray()

    val success = (0 until n).all { f[f[it]] == f[it] }
    if (!success) {
        println(-1); return
    }
    val p = (0 until n).filter { f[it] == it }.map { f[it] }
    val q = p.withIndex().associate { (i, v) -> v to i }
    // h(x) = p(x)
    // g(x) = q(f(x))
    // -> h(g(x)) = p(q(f(x)) = f(x)
    // -> g(h(x)) = q(f(p(x))) = q(f(f(y))) = q(f(y)) = x

    val g = iar(n) { q[f[it]]!! }
    val m = p.size
    val h = p
    println(m)
    println(buildString { for (res in g) append("${res + 1} ") })
    println(buildString { for (res in h) append("${res + 1} ") })
}