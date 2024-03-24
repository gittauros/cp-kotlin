package com.tauros.cp.structure



/**
 * @author tauros
 */
// https://github.com/atcoder/ac-library/blob/master/document_en/lazysegtree.md
// ↑ atcoder的模板声明
// https://zhuanlan.zhihu.com/p/459579152
// https://zhuanlan.zhihu.com/p/459679512
// ↑ C++实现与中文说明
// https://codeforces.com/blog/entry/18051
// ↑ 线段树循环版，非递归线段树

// 使用时是半闭半开区间，区间端点必须从0开始
interface SegmentOpArray<D> {
    operator fun set(pos: Int, data: D)
    operator fun get(pos: Int): D
}
fun ceilingLog(num: Int) = num.takeHighestOneBit().let { if (it == num) it else it shl 1 }.countTrailingZeroBits()
class FuncLazySegmentTree<S, F>(val n: Int, init: ((Int) -> S)? = null,
                                private val op: (S, S) -> S, private val e: () -> S, private val mapping: S.(tag: F) -> S,
                            // f是标记，合并后相当于先用this，再用传入的
                                private val composition: F.(F) -> F, private val id: () -> F,
                                private val data: SegmentOpArray<S>, private val lazy: SegmentOpArray<F>) {
    private val log = ceilingLog(n)
    private val size = 1 shl log
    init {
        if (init != null) for (i in 0 until n) data[i + size] = init(i)
        for (i in size - 1 downTo 1) pushUp(i)
    }
    private fun accept(p: Int, f: F) {
        data[p] = data[p].mapping(f)
        if (p < size) lazy[p] = lazy[p].composition(f)
    }
    private fun pushDown(p: Int) {
        accept(p shl 1, lazy[p])
        accept(p shl 1 or 1, lazy[p])
        lazy[p] = id()
    }
    private fun pushUp(p: Int) {
        data[p] = op(data[p shl 1], data[p shl 1 or 1])
    }
    operator fun set(pos: Int, x: S) {
        val p = pos + size
        for (i in log downTo 1) pushDown(p shr i)
        data[p] = x
        for (i in 1 .. log) pushUp(p shr i)
    }
    operator fun get(pos: Int): S {
        val p = pos + size
        for (i in log downTo 1) pushDown(p shr i)
        return data[p]
    }
    fun prod(st: Int, ed: Int): S {
        if (st == ed) return e()
        var (cl, cr) = st + size to ed + size
        val (clo, cro) = cl.countTrailingZeroBits() to cr.countTrailingZeroBits()
        for (i in log downTo 1) {
            if (clo < i) pushDown(cl shr i)
            if (cro < i) pushDown(cr - 1 shr i)
        }
        var (l, r) = e() to e()
        while (cl < cr) {
            if (cl and 1 == 1) l = op(l, data[cl++])
            if (cr and 1 == 1) r = op(data[--cr], r)
            cl = cl shr 1; cr = cr shr 1
        }
        return op(l, r)
    }
    fun prodAll() = data[1]
    fun apply(pos: Int, f: F) {
        val p = pos + size
        for (i in log downTo 1) pushDown(p shr i)
        data[p] = data[p].mapping(f)
        for (i in 1 .. log) pushUp(p shr i)
    }
    fun apply(st: Int, ed: Int, f: F) {
        if (st == ed) return
        val (cl, cr) = st + size to ed + size
        val (clo, cro) = cl.countTrailingZeroBits() to cr.countTrailingZeroBits()
        for (i in log downTo 1) {
            if (clo < i) pushDown(cl shr i)
            if (cro < i) pushDown(cr - 1 shr i)
        }
        var (l, r) = cl to cr
        while (l < r) {
            if (l and 1 == 1) accept(l++, f)
            if (r and 1 == 1) accept(--r, f)
            l = l shr 1; r = r shr 1
        }
        for (i in 1 .. log) {
            if (clo < i) pushUp(cl shr i)
            if (cro < i) pushUp(cr - 1 shr i)
        }
    }
}

fun <S, F> funcLazySeg(
    n: Int, init: (Int) -> S, op: (S, S) -> S, e: () -> S,
    mapping: S.(tag: F) -> S, composition: F.(F) -> F, id: () -> F,
    newDataArray: (Int) -> SegmentOpArray<S>, newLazyArray: (Int) -> SegmentOpArray<F>
): FuncLazySegmentTree<S, F> {
    val size = 1 shl ceilingLog(n)
    val dataArray = newDataArray(size shl 1)
    val lazyArray = newLazyArray(size)
    return FuncLazySegmentTree(n, init, op, e, mapping, composition, id, dataArray, lazyArray)
}

// 自己的版本，比上面的时间快些，内存大些
// 使用时是闭区间
interface LazySegmentData<D : LazySegmentData<D, T>, T> {
    var tag: T
    fun tagAvailable(): Boolean
    fun clearTag()
    fun acceptTag(other: T)
    fun update(l: D, r: D)
}
data class LazySegmentTreeNode<D>(val cl: Int, val cr: Int, val info: D, val mid: Int = cl + cr shr 1) {
    var l: LazySegmentTreeNode<D>? = null
    var r: LazySegmentTreeNode<D>? = null
}
class LazySegmentTree<D : LazySegmentData<D, T>, T>(start: Int, end: Int, val defaultData: () -> D) {
    val q = ArrayDeque<LazySegmentTreeNode<D>>()
    private fun LazySegmentTreeNode<D>.build(init: D.(Int) -> Unit) {
        if (cl == cr) { info.init(cl); return }
        l = LazySegmentTreeNode(cl, mid, defaultData())
        r = LazySegmentTreeNode(mid + 1, cr, defaultData())
        l!!.build(init); r!!.build(init)
        pushUp()
    }
    private fun LazySegmentTreeNode<D>.pushDown() {
        if (cl == cr) return
        if (l == null) l = LazySegmentTreeNode(cl, mid, defaultData())
        if (r == null) r = LazySegmentTreeNode(mid + 1, cr, defaultData())
        if (info.tagAvailable()) {
            l!!.info.acceptTag(info.tag)
            r!!.info.acceptTag(info.tag)
            info.clearTag()
        }
    }
    fun LazySegmentTreeNode<D>.pushUp() { info.update(l!!.info, r!!.info) }
    private fun LazySegmentTreeNode<D>.update(st: Int, ed: Int, upd: T) {
        if (cl >= st && cr <= ed) { info.acceptTag(upd); return }
        pushDown()
        if (st <= mid) l!!.update(st, ed, upd)
        if (ed > mid) r!!.update(st, ed, upd)
        pushUp()
    }
    private fun <R> LazySegmentTreeNode<D>.query(st: Int, ed: Int, query: D.() -> R, merge: (R, R) -> R): R {
        if (cl >= st && cr <= ed) return info.query()
        pushDown()
        if (ed <= mid) return l!!.query(st, ed, query, merge)
        if (st > mid) return r!!.query(st, ed, query, merge)
        return merge(l!!.query(st, ed, query, merge), r!!.query(st, ed, query, merge))
    }
    var seg = LazySegmentTreeNode(start, end, defaultData())
    fun build(init: D.(Int) -> Unit) = seg.build(init)
    fun update(st: Int, ed: Int, upd: T) = seg.update(st, ed, upd)
    fun <R> query(st: Int, ed: Int, query: D.() -> R, merge: (R?, R?) -> R) = seg.query(st, ed, query, merge)!!
    fun <R> queryAll(query: D.() -> R) = seg.info.query()
}