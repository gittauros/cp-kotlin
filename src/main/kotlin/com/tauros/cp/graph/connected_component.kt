package com.tauros.cp.graph


/**
 * @author tauros
 */
class Bridge<G : Graph>(val g: G, val nodeCnt: Int = g.nodeCap) {
    val isBridge = BooleanArray(g.totalEdgeCnt)
    val bridgeIds: List<Int>
    private val dfn = IntArray(nodeCnt)
    private val low = IntArray(nodeCnt)
    private val parent = IntArray(nodeCnt)
    private var idx = 0

    init {
        for (i in 0 until nodeCnt) if (dfn[i] == 0) {
            tarjan(i)
        }
        bridgeIds = buildList {
            g.each {
                if (isBridge[it]) add(it)
            }
        }
    }

    private fun tarjan(u: Int) {
        dfn[u] = ++idx; low[u] = dfn[u]
        g.each(u) {
            val v = g.to[it]
            if (dfn[v] == 0) {
                parent[v] = u
                tarjan(v)
                low[u] = minOf(low[u], low[v])
                if (low[v] > dfn[u]) {
                    isBridge[it] = true; isBridge[it xor 1] = true
                }
            } else if (parent[u] != v) {
                low[u] = minOf(low[u], dfn[v])
            }
        }
    }
}

fun <G : Graph> G.bridges(nodeCnt: Int = nodeCap) = Bridge(this, nodeCnt)