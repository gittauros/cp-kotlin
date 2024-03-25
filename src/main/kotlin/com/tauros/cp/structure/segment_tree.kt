package com.tauros.cp.structure



/**
 * @author tauros
 */
// https://github.com/atcoder/ac-library/blob/master/document_en/lazysegtree.md
// https://github.com/atcoder/ac-library/blob/master/atcoder/lazysegtree.hpp
// ↑ atcoder的模板
// https://zhuanlan.zhihu.com/p/459579152
// https://zhuanlan.zhihu.com/p/459679512
// ↑ C++实现与中文说明
// https://codeforces.com/blog/entry/18051
// ↑ 线段树循环版，非递归线段树

// 使用时是半闭半开区间，区间端点必须从0开始
interface LazySegmentNode<D : LazySegmentNode<D, T>, T> {
    var tag: T
    fun tagAvailable(): Boolean
    fun clearTag()
    fun acceptTag(other: T)
    fun update(l: D, r: D)
}
fun ceilingLog(num: Int) = num.takeHighestOneBit().let { if (it == num) it else it shl 1 }.countTrailingZeroBits()
class LazySegment<D : LazySegmentNode<D, T>, T>(val n: Int, newArray: (Int) -> Array<D>, init: D.(Int) -> Unit = { }) {
    val log = ceilingLog(n)
    val size = 1 shl log
    val nodes: Array<D>
    init {
        nodes = newArray(size * 2)
        for (i in 0 until n) nodes[i + size].init(i)
        for (i in size - 1 downTo 1) pushUp(i)
    }
    fun pushDown(p: Int) {
        if (nodes[p].tagAvailable()) {
            nodes[p shl 1].acceptTag(nodes[p].tag)
            nodes[p shl 1 or 1].acceptTag(nodes[p].tag)
            nodes[p].clearTag()
        }
    }
    private fun pushUp(p: Int) {
        nodes[p].update(nodes[p shl 1], nodes[p shl 1 or 1])
    }
    operator fun set(pos: Int, d: D) {
        val p = pos + size
        for (i in log downTo 1) pushDown(p shr i)
        nodes[p] = d
        for (i in 1 .. log) pushUp(p shr i)
    }
    operator fun get(pos: Int): D {
        val p = pos + size
        for (i in log downTo 1) pushDown(p shr i)
        return nodes[p]
    }
    inline fun <R> query(st: Int, ed: Int, query: D.() -> R, merge: (R, R) -> R): R {
        var (cl, cr) = st + size to ed + size
        val (clo, cro) = cl.countTrailingZeroBits() to cr.countTrailingZeroBits()
        for (i in log downTo 1) {
            if (clo < i) pushDown(cl shr i)
            if (cro < i) pushDown(cr - 1 shr i)
        }
        var (l, r) = null as R? to null as R?
        while (cl < cr) {
            if (cl and 1 == 1) l = if (l == null) nodes[cl++].query() else merge(l, nodes[cl++].query())
            if (cr and 1 == 1) r = if (r == null) nodes[--cr].query() else merge(nodes[--cr].query(), r)
            cl = cl shr 1; cr = cr shr 1
        }
        return if (l == null) r!! else if (r == null) l else merge(l, r)
    }
    inline fun <R> queryAll(query: D.() -> R) = nodes[1].query()
    fun update(pos: Int, upd: T) {
        val p = pos + size
        for (i in log downTo 1) pushDown(p shr i)
        nodes[p].acceptTag(upd)
        for (i in 1 .. log) pushUp(p shr i)
    }
    fun update(st: Int, ed: Int, upd: T) {
        if (st == ed) return
        val (cl, cr) = st + size to ed + size
        val (clo, cro) = cl.countTrailingZeroBits() to cr.countTrailingZeroBits()
        for (i in log downTo 1) {
            if (clo < i) pushDown(cl shr i)
            if (cro < i) pushDown(cr - 1 shr i)
        }
        var (l, r) = cl to cr
        while (l < r) {
            if (l and 1 == 1) nodes[l++].acceptTag(upd)
            if (r and 1 == 1) nodes[--r].acceptTag(upd)
            l = l shr 1; r = r shr 1
        }
        for (i in 1 .. log) {
            if (clo < i) pushUp(cl shr i)
            if (cro < i) pushUp(cr - 1 shr i)
        }
    }
}

typealias SegNode<D, T> = LazySegmentNode<D, T>
typealias Seg<D, T> = LazySegment<D, T>

// 自己的版本，树版本
// 使用时是闭区间
abstract class LazySegmentTreeNode<D : LazySegmentTreeNode<D, T>, T>(val cl: Int, val cr: Int, val mid: Int = cl + cr shr 1) : LazySegmentNode<D, T> {
    var l: D? = null
    var r: D? = null
}
class LazySegmentTree<D : LazySegmentTreeNode<D, T>, T>(start: Int, end: Int, val newNode: (cl: Int, cr: Int) -> D) {
    private fun D.build(init: D.(Int) -> Unit) {
        if (cl == cr) { init(cl); return }
        l = newNode(cl, mid)
        r = newNode(mid + 1, cr)
        l!!.build(init); r!!.build(init)
        pushUp()
    }
    private fun D.pushDown() {
        if (cl == cr) return
        if (l == null) l = newNode(cl, mid)
        if (r == null) r = newNode(mid + 1, cr)
        if (tagAvailable()) {
            l!!.acceptTag(tag)
            r!!.acceptTag(tag)
            clearTag()
        }
    }
    fun D.pushUp() { update(l!!, r!!) }
    private fun D.update(st: Int, ed: Int, upd: T) {
        if (cl >= st && cr <= ed) { acceptTag(upd); return }
        pushDown()
        if (st <= mid) l!!.update(st, ed, upd)
        if (ed > mid) r!!.update(st, ed, upd)
        pushUp()
    }
    private fun <R> D.query(st: Int, ed: Int, query: D.() -> R, merge: (R, R) -> R): R {
        if (cl >= st && cr <= ed) return query()
        pushDown()
        if (ed <= mid) return l!!.query(st, ed, query, merge)
        if (st > mid) return r!!.query(st, ed, query, merge)
        return merge(l!!.query(st, ed, query, merge), r!!.query(st, ed, query, merge))
    }
    val seg = newNode(start, end)
    fun build(init: D.(Int) -> Unit) = seg.build(init)
    fun update(st: Int, ed: Int, upd: T) = seg.update(st, ed, upd)
    fun <R> query(st: Int, ed: Int, query: D.() -> R, merge: (R, R) -> R) = seg.query(st, ed, query, merge)!!
    fun <R> queryAll(query: D.() -> R) = seg.query()
}

typealias SegTreeNode<D, T> = LazySegmentTreeNode<D, T>
typealias SegTree<D, T> = LazySegmentTree<D, T>