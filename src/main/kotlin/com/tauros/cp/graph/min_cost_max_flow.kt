package com.tauros.cp.graph

import com.tauros.cp.structure.IIHeap


/**
 * @author tauros
 * 2023/9/28
 */
// graph

class MCMFIGraph(nodeCap: Int, edgeCap: Int = maxOf(nodeCap, MIN_EDGE_COUNT) * 2, needFrom: Boolean = false)
    : Graph(nodeCap, edgeCap, needFrom) {
    var vol = IntArray(edgeCap)
    var cost = IntArray(edgeCap)

    override fun expand(increment: Int) {
        super.expand(increment)
        vol += IntArray(increment)
        cost += IntArray(increment)
    }

    fun addEdge(from: Int, to: Int, v: Int = 0, c: Int = 0): Int {
        val edgeId = addEdge(from, to)
        vol[edgeId] = v
        cost[edgeId] = c
        return edgeId
    }

    fun addEdgeResidual(from: Int, to: Int, v: Int, c: Int): Int {
        val edgeId = addEdge(from, to, v, c)
        addEdge(to, from, 0, -c)
        return edgeId
    }
}

class MCMFLGraph(nodeCap: Int, edgeCap: Int = maxOf(nodeCap, MIN_EDGE_COUNT) * 2, needFrom: Boolean = false)
    : Graph(nodeCap, edgeCap, needFrom) {
    var vol = LongArray(edgeCap)
    var cost = LongArray(edgeCap)

    override fun expand(increment: Int) {
        super.expand(increment)
        vol += LongArray(increment)
        cost += LongArray(increment)
    }

    fun addEdge(from: Int, to: Int, v: Long = 0, c: Long = 0): Int {
        val edgeId = addEdge(from, to)
        vol[edgeId] = v
        cost[edgeId] = c
        return edgeId
    }

    fun addEdgeResidual(from: Int, to: Int, v: Long, c: Long): Int {
        val edgeId = addEdge(from, to, v, c)
        addEdge(to, from, 0, -c)
        return edgeId
    }
}

// min cost max flow

abstract class MCMFIDinic(val source: Int, val target: Int, nodeCap: Int) {
    var cur = IntArray(nodeCap)
    val queue = IntArray(nodeCap)
    val vis = BooleanArray(nodeCap)
    val dist = IntArray(nodeCap)
    abstract val INF: Int

    fun MCMFIGraph.spfa(): Boolean {
        dist.fill(INF)
        var head = 0
        var tail = 0

        dist[source] = 0
        queue[tail++] = source
        vis[source] = true
        var size = 1
        while (size > 0) {
            val u = queue[head]
            head = (head + 1) % queue.size
            size--
            vis[u] = false
            each(u) {
                val v = to[it]
                val newDist = dist[u] + cost[it]
                if (vol[it] > 0 && dist[v] > newDist) {
                    dist[v] = newDist
                    if (!vis[v]) {
                        queue[tail] = v
                        tail = (tail + 1) % queue.size
                        size++
                        vis[v] = true
                    }
                }
            }
        }

        return dist[target] != INF
    }

    fun MCMFIGraph.dfs(u: Int, flowCap: Int): IntArray {
        var flow = flowCap
        if (u == target || flow <= 0) {
            return intArrayOf(flow, 0)
        }
        vis[u] = true
        var totalFlow = 0
        var totalCost = 0
        eachBreakable(u, cur[u]) {
            if (flow <= 0) return@eachBreakable true
            cur[u] = it
            val v = to[it]
            if (vol[it] <= 0 || vis[v] || dist[v] != dist[u] + cost[it]) return@eachBreakable false
            val (nextFlow, nextCost) = dfs(v, minOf(vol[it], flow))
            if (nextFlow <= 0) return@eachBreakable false
            totalFlow += nextFlow
            flow -= nextFlow
            totalCost += nextFlow * cost[it] + nextCost
            vol[it] -= nextFlow
            vol[it xor 1] += nextFlow
            false
        }
        vis[u] = false
        return intArrayOf(totalFlow, totalCost)
    }

    abstract fun flow(): IntArray
    inline fun flowAll(withFlow: (Int, Int) -> Unit = { _, _ -> }): IntArray {
        var totalFlow = 0
        var totalCost = 0
        var f: IntArray
        while (flow().also { f = it }[0] >= 0) {
            val (flow, cost) = f
            totalFlow += flow
            totalCost += cost
            withFlow(flow, cost)
        }
        return intArrayOf(totalFlow, totalCost)
    }
}

fun MCMFIGraph.mcmfDinic(source: Int, target: Int) = object : MCMFIDinic(source, target, nodeCap) {
    override val INF = 1e9.toInt()

    override fun flow(): IntArray {
        if (!spfa()) return intArrayOf(-1, -1)
        first.copyInto(cur)
        return dfs(source, INF)
    }
}

abstract class MCMFIPrimalDual(val source: Int, val target: Int, nodeCap: Int, edgeCap: Int, val safe: Boolean = false) {
    var cur = IntArray(nodeCap)
    val dist = IntArray(nodeCap)
    val height = IntArray(nodeCap)
    val vis = BooleanArray(nodeCap)
    var useDij = false
    val pq = IIHeap(edgeCap) { a, b -> a.compareTo(b) }
    abstract val INF: Int

    infix fun Int.da(other: Int): Int = if (this >= INF || other >= INF) INF else minOf(INF, this + other)
    infix fun Int.dm(other: Int): Int = if (this >= INF) INF else if (this <= -INF || other >= INF) -INF else maxOf(-INF, this - other)

    fun MCMFIGraph.shortestPath(): Boolean {
        if (useDij) {
            for (i in 0 until nodeCap) height[i] = height[i] da dist[i]
        }
        dist.fill(INF)

        dist[source] = 0
        if (!useDij) {
            // spfa
            var (head, tail) = 0 to 0
            cur[tail++] = source
            vis[source] = true
            var size = 1
            while (size > 0) {
                val u = cur[head]
                head = (head + 1) % cur.size
                size--
                vis[u] = false
                each(u) {
                    val v = to[it]
                    val vDist = if (safe) dist[u] da cost[it] else dist[u] + cost[it]
                    if (vol[it] > 0 && vDist < dist[v]) {
                        dist[v] = vDist
                        if (!vis[v]) {
                            cur[tail] = v
                            tail = (tail + 1) % cur.size
                            size++
                            vis[v] = true
                        }
                    }
                }
            }
            useDij = true
        } else {
            // dijkstra
            pq.clear()
            pq.offer(dist[source] to source)
            var meet = 0
            while (pq.isNotEmpty() && meet < nodeCap) {
                val (uDist, u) = pq.poll()
                if (vis[u]) continue
                meet++
                vis[u] = true
                each(u) {
                    val v = to[it]
                    if (vol[it] > 0) {
                        val vDist = if (safe) {
                            uDist da cost[it] da height[u] dm height[v]
                        } else {
                            uDist + cost[it] + height[u] - height[v]
                        }
                        if (vDist < dist[v]) {
                            dist[v] = vDist
                            pq.offer(vDist to v)
                        }
                    }
                }
            }
            vis.fill(false)
        }

        return dist[target] != INF
    }

    fun MCMFIGraph.dfs(u: Int, flowCap: Int): IntArray {
        var flow = flowCap
        if (u == target || flow <= 0) {
            return intArrayOf(flow, 0)
        }
        var totalFlow = 0
        var totalCost = 0
        vis[u] = true
        eachBreakable(u, cur[u]) {
            if (flow <= 0) return@eachBreakable true
            cur[u] = it
            val v = to[it]
            if (vol[it] <= 0 || vis[v]) return@eachBreakable false
            val should = if (safe) {
                dist[u] da cost[it] da if(useDij) height[u] dm height[v] else 0
            } else {
                dist[u] + cost[it] + if(useDij) height[u] - height[v] else 0
            }
            if (dist[v] != should) return@eachBreakable false
            val (nextFlow, nextCost) = dfs(v, minOf(vol[it], flow))
            if (nextFlow <= 0) return@eachBreakable false
            totalFlow += nextFlow
            flow -= nextFlow
            totalCost += nextFlow * cost[it] + nextCost
            vol[it] -= nextFlow
            vol[it xor 1] += nextFlow
            false
        }
        vis[u] = false
        return intArrayOf(totalFlow, totalCost)
    }

    abstract fun flow(): IntArray
    inline fun flowAll(withFlow: (Int, Int) -> Unit = { _, _ -> }): IntArray {
        var totalFlow = 0
        var totalCost = 0
        var f: IntArray
        while (flow().also { f = it }[0] >= 0) {
            val (flow, cost) = f
            totalFlow += flow
            totalCost += cost
            withFlow(flow, cost)
        }
        return intArrayOf(totalFlow, totalCost)
    }
}

fun MCMFIGraph.mcmfPrimalDual(source: Int, target: Int, safe: Boolean = false) = object : MCMFIPrimalDual(source, target, nodeCap, edgeCap, safe) {
    override val INF = 1e9.toInt()

    override fun flow(): IntArray {
        if (!shortestPath()) return intArrayOf(-1, -1)
        first.copyInto(cur)
        return dfs(source, INF)
    }
}