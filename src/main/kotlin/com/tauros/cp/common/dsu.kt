package com.tauros.cp.common


/**
 * @author tauros
 * 2023/8/25
 */
class DSU(val n: Int) {
    private val parent = IntArray(n) { -1 }
    fun find(x: Int): Int =
        if (parent[x] < 0) x else find(parent[x]).also { parent[x] = it }
    fun size(x: Int): Int = -parent[find(x)]
    fun merge(x: Int, y: Int, x2y: Boolean = true): Boolean {
        val (leaderX, leaderY) = find(x) to find(y)
        if (leaderX == leaderY) return false
        val (from, to) =
            if (x2y || size(leaderX) <= size(leaderY)) leaderX to leaderY
            else leaderY to leaderX
        parent[to] += parent[from]
        parent[from] = to
        return true
    }
}