package com.tauros.cp.graph

import com.tauros.cp.common.minOf


/**
 * @author tauros
 * 2023/9/26
 */
// graph

class MFIGraph(nodeCap: Int, edgeCap: Int = maxOf(nodeCap, MIN_EDGE_COUNT) * 2, needFrom: Boolean = false)
    : Graph(nodeCap, edgeCap, needFrom) {
    var vol = IntArray(edgeCap)

    override fun expand(increment: Int) {
        super.expand(increment)
        vol += IntArray(increment)
    }

    fun addEdge(from: Int, to: Int, v: Int = 0): Int {
        val edgeId = addEdge(from, to)
        vol[edgeId] = v
        return edgeId
    }

    fun addEdgeResidual(from: Int, to: Int, v: Int): Int {
        val edgeId = addEdge(from, to, v)
        addEdge(to, from, 0)
        return edgeId
    }
}

class MFLGraph(nodeCap: Int, edgeCap: Int = maxOf(nodeCap, MIN_EDGE_COUNT) * 2, needFrom: Boolean = false)
    : Graph(nodeCap, edgeCap, needFrom) {
    var vol = LongArray(edgeCap)

    override fun expand(increment: Int) {
        super.expand(increment)
        vol += LongArray(increment)
    }

    fun addEdge(from: Int, to: Int, v: Long = 0): Int {
        val edgeId = addEdge(from, to)
        vol[edgeId] = v
        return edgeId
    }

    fun addEdgeResidual(from: Int, to: Int, v: Long): Int {
        val edgeId = addEdge(from, to, v)
        addEdge(to, from, 0)
        return edgeId
    }
}

// max flow

abstract class MFIDinic(val source: Int, val target: Int, nodeCap: Int) {
    var cur = IntArray(nodeCap)
    val queue = IntArray(nodeCap)
    val layer = IntArray(nodeCap)
    val vis = BooleanArray(nodeCap)

    fun MFIGraph.bfs(): Boolean {
        vis.fill(false)
        var head = 0
        var tail = 0
        layer[source] = 0
        queue[tail++] = source
        vis[source] = true
        while (head < tail) {
            val u = queue[head++]
            each(u) {
                val v = to[it]
                if (vol[it] > 0 && !vis[v]) {
                    vis[v] = true
                    layer[v] = layer[u] + 1
                    queue[tail++] = v
                }
            }
        }

        return vis[target]
    }

    fun MFIGraph.dfs(u: Int, flowCap: Int): Int {
        var flow = flowCap
        if (u == target || flow == 0) {
            return flow
        }
        var total = 0
        eachBreakable(u, cur[u]) {
            if (flow <= 0) return@eachBreakable true
            cur[u] = it
            val v = to[it];
            if (vol[it] <= 0 || layer[v] != layer[u] + 1) return@eachBreakable false
            val c = dfs(v, minOf(vol[it], flow))
            if (c > 0) {
                total += c
                flow -= c
                vol[it] -= c
                vol[it xor 1] += c
            }
            false
        }
        return total
    }

    abstract fun flow(): Int
    inline fun flowAll(withFlow: (Int) -> Unit = {}): Int {
        var totalFlow = 0
        var f: Int
        while (flow().also { f = it } >= 0) {
            totalFlow += f
            withFlow(f)
        }
        return totalFlow
    }
}

fun MFIGraph.mfDinic(source: Int, target: Int) = object : MFIDinic(source, target, nodeCap) {
    val INF = 1e9.toInt()

    override fun flow(): Int {
        if (!bfs()) return -1
        first.copyInto(cur)
        return dfs(source, INF)
    }
}

abstract class MFLDinic(val source: Int, val target: Int, nodeCap: Int) {
    var cur = IntArray(nodeCap)
    val queue = IntArray(nodeCap)
    val layer = IntArray(nodeCap)
    val vis = BooleanArray(nodeCap)

    fun MFLGraph.bfs(): Boolean {
        vis.fill(false)
        var head = 0
        var tail = 0
        layer[source] = 0
        queue[tail++] = source
        vis[source] = true
        while (head < tail) {
            val u = queue[head++]
            each(u) {
                val v = to[it]
                if (vol[it] > 0 && !vis[v]) {
                    vis[v] = true
                    layer[v] = layer[u] + 1
                    queue[tail++] = v
                }
            }
        }

        return vis[target]
    }

    fun MFLGraph.dfs(u: Int, flowCap: Long): Long {
        var flow = flowCap
        if (u == target || flow == 0L) {
            return flow
        }
        var total = 0L
        eachBreakable(u, cur[u]) {
            if (flow <= 0) return@eachBreakable true
            cur[u] = it
            val v = to[it];
            if (vol[it] <= 0 || layer[v] != layer[u] + 1) return@eachBreakable false
            val c = dfs(v, minOf(vol[it], flow))
            if (c > 0) {
                total += c
                flow -= c
                vol[it] -= c
                vol[it xor 1] += c
            }
            false
        }
        return total
    }

    abstract fun flow(): Long
    inline fun flowAll(withFlow: (Long) -> Unit = {}): Long {
        var totalFlow = 0L
        var f: Long
        while (flow().also { f = it } >= 0) {
            totalFlow += f
            withFlow(f)
        }
        return totalFlow
    }
}

fun MFLGraph.mfDinic(source: Int, target: Int) = object : MFLDinic(source, target, nodeCap) {
    val INF = 1e18.toLong()

    override fun flow(): Long {
        if (!bfs()) return -1
        first.copyInto(cur)
        return dfs(source, INF)
    }
}