package com.tauros.cp.common

import com.tauros.cp.graph.Graph
import kotlin.math.abs
import kotlin.random.Random


/**
 * @author tauros
 * 2023/9/5
 */
class Buffer<T>(
    val initCap: Int = 10,
    val supplier: () -> T
) {
    private var stack = ArrayDeque<T>(initCap)

    fun alloc(init: ((T) -> Unit)? = null): T {
        val obj = if (stack.isEmpty()) supplier.invoke() else stack.removeLast()
        return obj.also { init?.invoke(it) }
    }

    fun release(obj: T) {
        stack.addLast(obj)
    }
}

class TGenerator<T>(private var start: T, private val step: (T) -> T) : Iterable<T> {
    var end: (T) -> Boolean = { false }
    override fun iterator() = object : Iterator<T> {
        var cur = start
        val judge = end
        var took = false
        override fun hasNext() = if (judge.invoke(start)) false else if (!took) true else {
            cur = step.invoke(cur)
            if (judge.invoke(cur)) {
                false
            } else {
                took = false
                true
            }
        }
        override fun next(): T {
            assert(hasNext())
            took = true
            return cur
        }
    }
}

infix fun <T> T.iter(step: (T) -> T) = TGenerator(this, step)

infix fun <T> TGenerator<T>.until(end: (T) -> Boolean): TGenerator<T> {
    this.end = end
    return this
}

infix fun TGenerator<Int>.lt(end: Int): TGenerator<Int> {
    return until { it >= end }
}

infix fun TGenerator<Int>.le(end: Int): TGenerator<Int> {
    return until { it > end }
}

infix fun TGenerator<Int>.gt(end: Int): TGenerator<Int> {
    return until { it <= end }
}

infix fun TGenerator<Int>.ge(end: Int): TGenerator<Int> {
    return until { it < end }
}

infix fun TGenerator<Long>.lt(end: Long): TGenerator<Long> {
    return until { it >= end }
}

infix fun TGenerator<Long>.le(end: Long): TGenerator<Long> {
    return until { it > end }
}

infix fun TGenerator<Long>.gt(end: Long): TGenerator<Long> {
    return until { it <= end }
}

infix fun TGenerator<Long>.ge(end: Long): TGenerator<Long> {
    return until { it < end }
}

fun randomTree(nodeCnt: Int): Graph {
    val dsu = DSU(nodeCnt)
    val graph = Graph(nodeCnt, (nodeCnt - 1) * 2, true)
    var rest = nodeCnt
    while (rest > 1) {
        val (i, j) = abs(Random.nextInt()) % nodeCnt to abs(Random.nextInt()) % nodeCnt
        if (dsu.find(i) != dsu.find(j)) {
            graph.addEdge(i, j)
            graph.addEdge(j, i)
            dsu.merge(i, j)
            rest--
        }
    }
    return graph
}