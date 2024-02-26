package com.tauros.cp.common


/**
 * @author tauros
 * 2023/9/6
 */
class Linked<T> : Iterable<T> {
    private val HEAD = Node<T>()
    private val TAIL = Node<T>()
    private var cnt = 0
    private val size: Int
        get() = cnt

    init {
        connect(HEAD, TAIL)
    }

    internal data class Node<T>(
        var data: T? = null,
        var prev: Node<T>? = null,
        var next: Node<T>? = null
    )

    private fun <T> connect(prev: Node<T>?, next: Node<T>?) {
        prev?.next = next
        next?.prev = prev
    }

    fun addLast(d: T) {
        val node = Node(d)
        cnt++
        connect(TAIL.prev, node)
        connect(node, TAIL)
    }

    fun addFirst(d: T) {
        val node = Node(d)
        cnt++
        connect(node, HEAD.next)
        connect(HEAD, node)
    }

    fun removeLast(): T {
        assert(isNotEmpty())
        cnt--
        val d = TAIL.prev!!.data!!
        connect(TAIL.prev!!.prev!!, TAIL)
        return d
    }

    fun removeFirst(): T {
        assert(isNotEmpty())
        cnt--
        val d = HEAD.next!!.data!!
        connect(HEAD, HEAD.next!!.next!!)
        return d
    }

    fun isEmpty() = size == 0
    fun isNotEmpty() = size > 0

    fun clear() {
        connect(HEAD, TAIL)
        cnt = 0
    }

    override fun iterator(): Iterator<T> {
        var iter = HEAD.next
        return object : Iterator<T> {
            override fun hasNext(): Boolean {
                return iter != TAIL
            }

            override fun next(): T {
                val result = iter!!.data!!
                iter = iter!!.next
                return result
            }
        }
    }
}