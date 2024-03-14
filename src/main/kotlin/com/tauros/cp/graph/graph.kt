package com.tauros.cp.graph


/**
 * @author tauros
 * 2023/9/13
 */
open class Graph(val nodeCap: Int, var edgeCap: Int = maxOf(nodeCap, MIN_EDGE_COUNT),
                 private val needFrom: Boolean = false, private val removeAble: Boolean = false) {
    companion object {
        val MAX_EDGE_COUNT = 1e8.toInt()
        val MIN_EDGE_COUNT = 10
    }

    var idx = -1
    var removed = 0
    val totalEdgeCnt: Int get() = idx + 1 - removed
    val first = IntArray(nodeCap) { -1 }
    val vtxEdgeCnt = IntArray(nodeCap)
    var to = IntArray(edgeCap)
    var next = IntArray(edgeCap)
    var from = IntArray(if (needFrom) edgeCap else 0)

    private fun newCapacity(old: Int) = minOf(MAX_EDGE_COUNT, maxOf(MIN_EDGE_COUNT, old + (old shr 1)))

    private fun ensureCapacityIncrement() {
        if (idx + 1 >= edgeCap) {
            val newCap = newCapacity(edgeCap)
            expand(newCap - edgeCap)
            edgeCap = newCap
        }
    }

    protected open fun expand(increment: Int) {
        edgeCap += increment
        to += IntArray(increment)
        next += IntArray(increment)
        if (needFrom) {
            from += IntArray(increment)
        }
    }

    fun addEdge(from: Int, to: Int): Int {
        ensureCapacityIncrement()
        val edgeId = ++this.idx
        if (this.needFrom) {
            this.from[edgeId] = from
        }
        this.to[edgeId] = to
        this.next[edgeId] = this.first[from]
        this.first[from] = edgeId
        this.vtxEdgeCnt[from] += 1
        return edgeId
    }

    inline fun each(u: Int, start: Int = first[u], withEdgeId: (Int) -> Unit = {}) {
        var edgeId = start
        while (edgeId != -1) {
            withEdgeId(edgeId)
            edgeId = next[edgeId]
        }
    }

    inline fun each(withEdgeId: (Int) -> Unit = {}) {
        var edgeId = 0
        while (edgeId <= idx) {
            withEdgeId(edgeId)
            edgeId++
        }
    }

    inline fun eachBreakable(u: Int, start: Int = first[u], withEdgeId: (Int) -> Boolean = { false }) {
        var edgeId = start
        var isBreak = false
        while (edgeId != -1 && !isBreak) {
            isBreak = withEdgeId(edgeId)
            edgeId = next[edgeId]
        }
    }

    inline fun eachBreakable(withEdgeId: (Int) -> Boolean = { false }) {
        var edgeId = 0
        var isBreak = false
        while (edgeId <= idx && !isBreak) {
            isBreak = withEdgeId(edgeId)
            edgeId++
        }
    }

    fun clear(u: Int) {
        if (removeAble) {
            removed += vtxEdgeCnt[u]
            vtxEdgeCnt[u] = 0
            first[u] = -1
        }
    }
}

class IGraph(nodeCap: Int, edgeCap: Int = maxOf(nodeCap, MIN_EDGE_COUNT) * 2, needFrom: Boolean = false, removeAble: Boolean = false)
    : Graph(nodeCap, edgeCap, needFrom, removeAble) {
    var weight = IntArray(edgeCap)

    override fun expand(increment: Int) {
        super.expand(increment)
        weight += IntArray(increment)
    }

    fun addEdge(from: Int, to: Int, d: Int): Int {
        val edgeId = addEdge(from, to)
        weight[edgeId] = d
        return edgeId
    }
}

class LGraph(nodeCap: Int, edgeCap: Int = maxOf(nodeCap, MIN_EDGE_COUNT) * 2, needFrom: Boolean = false, removeAble: Boolean = false)
    : Graph(nodeCap, edgeCap, needFrom, removeAble) {
    var weight = LongArray(edgeCap)

    override fun expand(increment: Int) {
        super.expand(increment)
        weight += LongArray(increment)
    }

    fun addEdge(from: Int, to: Int, d: Long): Int {
        val edgeId = addEdge(from, to)
        weight[edgeId] = d
        return edgeId
    }
}

open class EdgeGraph<E>(val nodeCap: Int, var edgeCap: Int = maxOf(nodeCap, MIN_EDGE_COUNT), private val needFrom: Boolean = false) {
    companion object {
        val MAX_EDGE_COUNT = 1e8.toInt()
        val MIN_EDGE_COUNT = 10
    }

    var idx = -1
    val edgeCnt: Int get() = idx + 1
    val first = IntArray(nodeCap) { -1 }
    var next = IntArray(edgeCap)
    var from = IntArray(if (needFrom) edgeCap else 0)
    var edges = arrayOfNulls<Any?>(edgeCap)

    private fun newCapacity(old: Int) = minOf(MAX_EDGE_COUNT, maxOf(MIN_EDGE_COUNT, old + (old shr 1)))

    private fun ensureCapacityIncrement() {
        if (idx + 1 >= edgeCap) {
            val newCap = newCapacity(edgeCap)
            expand(newCap - edgeCap)
            edgeCap = newCap
        }
    }

    private fun expand(increment: Int) {
        edgeCap += increment
        next += IntArray(increment)
        val newEdgeArray = arrayOfNulls<Any?>(edges.size + increment)
        edges.copyInto(newEdgeArray, 0, 0, edges.size)
        edges = newEdgeArray
        if (needFrom) {
            from += IntArray(increment)
        }
    }

    fun addEdge(from: Int, edge: E): Int {
        ensureCapacityIncrement()
        val edgeId = ++this.idx
        if (this.needFrom) {
            this.from[edgeId] = from
        }
        this.edges[edgeId] = edge
        this.next[edgeId] = this.first[from]
        this.first[from] = edgeId
        return edgeId
    }

    @Suppress("UNCHECKED_CAST")
    open operator fun get(edgeId: Int): E = edges[edgeId] as E

    @Suppress("UNCHECKED_CAST")
    inline fun each(u: Int, start: Int = first[u], withEdgeId: EdgeGraph<E>.(Int) -> Unit = {}) {
        var edgeId = start
        while (edgeId != -1) {
            withEdgeId(this, edgeId)
            edgeId = next[edgeId]
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun each(withEdgeId: EdgeGraph<E>.(Int) -> Unit = {}) {
        var edgeId = 0
        while (edgeId <= idx) {
            withEdgeId(this, edgeId)
            edgeId++
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun eachBreakable(u: Int, start: Int = first[u], withEdgeId: EdgeGraph<E>.(Int) -> Boolean = { false }) {
        var edgeId = start
        var isBreak = false
        while (edgeId != -1 && !isBreak) {
            isBreak = withEdgeId(this, edgeId)
            edgeId = next[edgeId]
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun eachBreakable(withEdgeId: EdgeGraph<E>.(Int) -> Boolean = { false }) {
        var edgeId = 0
        var isBreak = false
        while (edgeId <= idx && !isBreak) {
            isBreak = withEdgeId(this, edgeId)
            edgeId++
        }
    }
}