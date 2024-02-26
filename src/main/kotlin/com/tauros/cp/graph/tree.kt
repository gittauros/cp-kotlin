package com.tauros.cp.graph

/**
 * @author tauros
 */
// 点分治
class DFZ<G : Graph>(val g: G, val nodeCnt: Int = g.nodeCap) {
    val size = IntArray(g.nodeCap)
    val deleted = BooleanArray(g.nodeCap)

    fun centroid(u: Int, fa: Int, total: Int): Int {
        size[u] = 1
        var maxSon = 0
        g.each(u) {
            val v = g.to[it]
            if (v == fa || deleted[v]) return@each
            val res = centroid(v, u, total)
            if (res != -1) return res
            maxSon = maxOf(maxSon, size[v])
            size[u] += size[v]
        }
        val upSon = total - size[u]
        maxSon = maxOf(maxSon, upSon)

        return if (maxSon + maxSon <= total) {
            if (fa in 0 until g.nodeCap) size[fa] = upSon
            size[u] = total
            u
        } else -1
    }

    fun calc(root: Int, calc: DFZ<G>.(Int) -> Unit) = calc(root, -1, nodeCnt, calc)

    fun calc(u: Int, fa: Int, total: Int, calc: DFZ<G>.(Int) -> Unit) {
        val centroid = centroid(u, fa, total)
        calc(centroid)

        deleted[centroid] = true
        g.each(centroid) {
            val v = g.to[it]
            if (v == fa || deleted[v]) return@each
            calc(v, centroid, size[v], calc)
        }
    }
}

fun <G : Graph> dfz(graph: G, root: Int = 0, calc: DFZ<G>.(Int) -> Unit) = DFZ(graph).calc(root, calc)

// 启发式合并
private fun Graph.dfn(root: Int): Array<IntArray> {
    val result = arrayOf(IntArray(this.nodeCap), IntArray(this.nodeCap), IntArray(this.nodeCap), IntArray(this.nodeCap))
    val (dfn, size, ori, son) = result
    son.fill(-1)
    var idx = 0
    fun Graph.dfs(u: Int, fa: Int) {
        dfn[u] = idx++
        ori[dfn[u]] = u
        size[u] = 1
        each(u) {
            val v = to[it]
            if (v == fa) return@each
            dfs(v, u)
            if (son[u] == -1 || size[v] > size[son[u]]) son[u] = v
            size[u] += size[v]
        }
    }
    dfs(root, -1)
    return result
}

class TreeInfo<G : Graph>(val g: G, val root: Int, val nodeCnt: Int = g.nodeCap) {
    val dfn: IntArray
    val size: IntArray
    val ori: IntArray
    val son: IntArray

    init {
        val result = g.dfn(root)
        dfn = result[0]
        size = result[1]
        ori = result[2]
        son = result[3]
    }

    fun dfnRange(u: Int) = dfn[u] until dfn[u] + size[u]
}

fun <G : Graph> G.dsu(root: Int, calc: TreeInfo<G>.(Int, Int, Boolean) -> Unit) {
    fun TreeInfo<G>.dsu(u: Int, fa: Int, keep: Boolean) {
        val son = son[u]
        each(u) {
            val v = to[it]
            if (v == fa || v == son) return@each
            dsu(v, u, false)
        }
        if (son != -1) dsu(son, u, true)

        calc(u, fa, keep)
    }
    val info = TreeInfo(this, root)
    info.dsu(root, -1, false)
}

// 重链剖分
class HLD<G : Graph>(val g: G, val root: Int, val nodeCnt: Int = g.nodeCap) {
    val dfn = IntArray(nodeCnt)
    val size = IntArray(nodeCnt)
    val ori = IntArray(nodeCnt)
    val son = IntArray(nodeCnt) { -1 }
    val top = IntArray(nodeCnt) { it }
    val dep = IntArray(nodeCnt)
    val parent = IntArray(nodeCnt)

    init {
        g.calcSon(root, -1)
        g.decompose(root, -1)
    }

    private fun Graph.calcSon(u: Int, fa: Int) {
        size[u] = 1
        each(u) {
            val v = to[it]
            if (v == fa) return@each
            calcSon(v, u)
            size[u] += size[v]
            if (son[u] == -1 || size[v] > size[son[u]]) {
                son[u] = v
            }
        }
    }

    private fun Graph.decompose(u: Int, fa: Int, d: Int = 0, curId: Int = 0) {
        var id = curId
        dfn[u] = id++; ori[dfn[u]] = u; parent[u] = fa; dep[u] = d
        if (son[u] != -1) {
            top[son[u]] = top[u]
            decompose(son[u], u, d + 1, id)
            id += size[son[u]]
        }
        each(u) {
            val v = to[it]
            if (v == fa || v == son[u]) return@each
            decompose(v, u, d + 1, id)
            id += size[v]
        }
    }

    fun lca(u: Int, v: Int): Int {
        var (x, y) = u to v
        while (top[x] != top[y]) {
            if (dep[top[x]] > dep[top[y]]) {
                x = parent[top[x]]
            } else {
                y = parent[top[y]]
            }
        }
        return if (dep[x] < dep[y]) x else y
    }
}

fun <G : Graph> G.hld(root: Int, nodeCnt: Int = nodeCap) = HLD(this, root, nodeCnt)

// 虚树 auxiliary/virtual tree
class VirtualTreeBuilder(val originGraph: Graph, val root: Int, val nodeCnt: Int = originGraph.nodeCap) {
    val tree = Graph(nodeCnt, removeAble = true)
    val hld = originGraph.hld(root, nodeCnt)
    val stack = IntArray(nodeCnt)

    fun buildByKeyNodes(keyNodes: Iterable<Int>): Graph {
        val sorted = keyNodes.sortedBy { hld.dfn[it] }
        tree.clear(root); stack[0] = root
        var top = 0
        for (u in sorted) if (u != 0) {
            val lca = hld.lca(u, stack[top])
            if (lca != stack[top]) {
                while (hld.dfn[lca] < hld.dfn[stack[top - 1]]) {
                    tree.addEdge(stack[top - 1], stack[top])
                    top -= 1
                }
                if (hld.dfn[lca] > hld.dfn[stack[top - 1]]) {
                    tree.clear(lca)
                    tree.addEdge(lca, stack[top])
                    stack[top] = lca
                } else {
                    tree.addEdge(lca, stack[top--])
                }
            }
            tree.clear(u)
            stack[++top] = u
        }
        for (i in top - 1 downTo 0) {
            tree.addEdge(stack[i], stack[i + 1])
        }
        return tree
    }
}

fun Graph.vtBuilder(root: Int, nodeCnt: Int = nodeCap) = VirtualTreeBuilder(this, root, nodeCnt)