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

// 使用时是半闭半开区间，查询右端点全是不包含的，区间端点必须从0开始，不支持动态开点
interface LazySegmentNode<D : LazySegmentNode<D, T>, T> {
    var tag: T
    fun tagAvailable(): Boolean
    fun clearTag()
    fun acceptTag(other: T)
    fun update(l: D, r: D)
}
interface LazySegmentNonTagNode<D : LazySegmentNonTagNode<D>> : LazySegmentNode<D, Unit> {
    override var tag: Unit
        get() = Unit
        set(v) {  }
    override fun tagAvailable() = false
    override fun clearTag() { }
    override fun acceptTag(other: Unit) { }
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
    fun pushUp(p: Int) {
        nodes[p].update(nodes[p shl 1], nodes[p shl 1 or 1])
    }
    inline fun update(pos: Int, upd: D.() -> Unit) {
        val p = pos + size
        for (i in log downTo 1) pushDown(p shr i)
        nodes[p].upd()
        for (i in 1 .. log) pushUp(p shr i)
    }
    inline fun <R> query(pos: Int, query: D.() -> R): R {
        val p = pos + size
        for (i in log downTo 1) pushDown(p shr i)
        return nodes[p].query()
    }
    fun query(pos: Int) = query(pos) { this }
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
    inline fun query(st: Int, ed: Int, merge: (D, D) -> D) = query(st, ed, { this }, merge)
    inline fun <R> queryAll(query: D.() -> R) = nodes[1].query()
    fun queryAll() = nodes[1]
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
    inline fun <R> first(ed: Int, query: D.() -> R, judge: (R) -> Boolean, merge: (R, R) -> R): Int {
        var cr = ed + size
        for (i in log downTo 1) pushDown(cr - 1 shr i)
        var sum = null as R?
        do {
            cr -= 1
            while (cr > 1 && cr and 1 == 1) cr = cr shr 1
            if (!judge(if (sum == null) nodes[cr].query() else merge(nodes[cr].query(), sum))) {
                while (cr < size) {
                    pushDown(cr)
                    cr = cr shl 1 or 1
                    val nex = if (sum == null) nodes[cr].query() else merge(nodes[cr].query(), sum)
                    if (judge(nex)) {
                        sum = nex; cr -= 1
                    }
                }
                return cr + 1 - size
            }
            sum = if (sum == null) nodes[cr].query() else merge(nodes[cr].query(), sum)
        } while (cr.takeLowestOneBit() != cr)
        return 0
    }
    inline fun first(ed: Int, judge: (D) -> Boolean, merge: (D, D) -> D) = first(ed, { this }, judge, merge)
    inline fun <R> last(st: Int, query: D.() -> R, judge: (R) -> Boolean, merge: (R, R) -> R): Int {
        var cl = st + size
        for (i in log downTo 1) pushDown(cl shr i)
        var sum = null as R?
        do {
            while (cl and 1 == 0) cl = cl shr 1
            if (!judge(if (sum == null) nodes[cl].query() else merge(sum, nodes[cl].query()))) {
                while (cl < size) {
                    pushDown(cl)
                    cl = cl shl 1
                    val nex = if (sum == null) nodes[cl].query() else merge(sum, nodes[cl].query())
                    if (judge(nex)) {
                        sum = nex; cl += 1
                    }
                }
                return cl - size;
            }
            sum = if (sum == null) nodes[cl].query() else merge(sum, nodes[cl].query())
            cl += 1
        } while (cl.takeLowestOneBit() != cl)
        return n
    }
    inline fun last(st: Int, judge: (D) -> Boolean, merge: (D, D) -> D) = last(st, { this }, judge, merge)
}

typealias SegTagNode<D, T> = LazySegmentNode<D, T>
typealias SegNonTagNode<D> = LazySegmentNonTagNode<D>
typealias Seg<D, T> = LazySegment<D, T>

// 自己的版本，树版本
// 使用时是闭区间，区间起点终点无要求，支持动态开点
abstract class LazySegmentTreeTagNode<D : LazySegmentTreeTagNode<D, T>, T>(val cl: Int, val cr: Int, val mid: Int = cl + cr shr 1) : LazySegmentNode<D, T> {
    var l: D? = null
    var r: D? = null
}
abstract class LazySegmentTreeNonTagNode<D : LazySegmentTreeNonTagNode<D>>(cl: Int, cr: Int, mid: Int = cl + cr shr 1) : LazySegmentTreeTagNode<D, Unit>(cl, cr, mid) {
    override var tag = Unit
    override fun tagAvailable() = false
    override fun clearTag() { }
    override fun acceptTag(other: Unit) { }
}
class LazySegmentTree<D : LazySegmentTreeTagNode<D, T>, T>(start: Int, end: Int, val newNode: (cl: Int, cr: Int) -> D) {
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
    private fun D.update(pos: Int, upd: D.() -> Unit) {
        if (cl == cr) { this.upd(); return }
        pushDown()
        if (pos <= mid) l!!.update(pos, upd)
        else r!!.update(pos, upd)
        pushUp()
    }
    private fun <R> D.query(pos: Int, query: D.() -> R): R {
        if (cl == cr) return query()
        pushDown()
        return if (pos <= mid) l!!.query(pos, query) else r!!.query(pos, query)
    }
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
    private fun <R> D.first(ed: Int, query: D.() -> R, judge: (R) -> Boolean, merge: (R, R) -> R): Pair<Int, R> {
        if (cl == cr) {
            val cur = query()
            return if (judge(cur)) cl to cur else cr + 1 to cur
        }
        pushDown()
        if (ed <= mid) return l!!.first(ed, query, judge, merge)
        val (end, right) = r!!.first(ed, query, judge, merge)
        if (end > mid + 1) return end to right
        val cur = merge(l!!.query(), right)
        if (judge(cur)) return cl to cur
        var (iter, sum) = l!! to right
        while (iter.cl != iter.cr) {
            iter.pushDown()
            val nex = merge(iter.r!!.query(), sum)
            if (judge(nex)) {
                sum = nex; iter = iter.l!!
            } else iter = iter.r!!
        }
        val nex = merge(iter.query(), sum)
        return if (judge(nex)) iter.cl to nex else iter.cr + 1 to sum
    }
    private fun <R> D.last(st: Int, query: D.() -> R, judge: (R) -> Boolean, merge: (R, R) -> R): Pair<Int, R> {
        if (cl == cr) {
            val cur = query()
            return if (judge(cur)) cr to cur else cl - 1 to cur
        }
        pushDown()
        if (st > mid) return r!!.last(st, query, judge, merge)
        val (end, left) = l!!.last(st, query, judge, merge)
        if (end < mid) return end to left
        val cur = merge(left, r!!.query())
        if (judge(cur)) return cr to cur
        var (iter, sum) = r!! to left
        while (iter.cl != iter.cr) {
            iter.pushDown()
            val nex = merge(sum, iter.l!!.query())
            if (judge(nex)) {
                sum = nex; iter = iter.r!!
            } else iter = iter.l!!
        }
        val nex = merge(sum, iter.query())
        return if (judge(nex)) iter.cr to nex else iter.cl - 1 to sum
    }
    val seg = newNode(start, end)
    fun build(init: D.(Int) -> Unit) = seg.build(init)
    fun update(pos: Int, upd: D.() -> Unit) = seg.update(pos, upd)
    fun <R> query(pos: Int, query: D.() -> R) = seg.query(pos, query)
    fun query(pos: Int) = seg.query(pos) { this }
    fun update(st: Int, ed: Int, upd: T) = seg.update(st, ed, upd)
    fun <R> query(st: Int, ed: Int, query: D.() -> R, merge: (R, R) -> R) = seg.query(st, ed, query, merge)
    fun query(st: Int, ed: Int, merge: (D, D) -> D) = seg.query(st, ed, { this }, merge)
    fun <R> queryAll(query: D.() -> R) = seg.query()
    fun queryAll() = seg
    fun <R> first(ed: Int, query: D.() -> R, judge: (R) -> Boolean, merge: (R, R) -> R) = seg.first(ed, query, judge, merge).first
    fun first(ed: Int, judge: (D) -> Boolean, merge: (D, D) -> D) = first(ed, { this }, judge, merge)
    fun <R> last(st: Int, query: D.() -> R, judge: (R) -> Boolean, merge: (R, R) -> R) = seg.last(st, query, judge, merge).first
    fun last(st: Int, judge: (D) -> Boolean, merge: (D, D) -> D) = first(st, { this }, judge, merge)
}

typealias SegTreeTagNode<D, T> = LazySegmentTreeTagNode<D, T>
typealias SegTreeNonTagNode<D> = LazySegmentTreeNonTagNode<D>
typealias SegTree<D, T> = LazySegmentTree<D, T>