package com.tauros.cp.graph


/**
 * @author tauros
 * 2023/10/9
 */
class KMI(val n: Int, val graph: Array<IntArray>, val inf: Int = 0x3f3f3f3f) {
    private val xVal = IntArray(n) { graph[it].reduce(::maxOf) }
    private val yVal = IntArray(n)
    private val stack = IntArray(n)
    private val newMatchY = IntArray(n)
    private val xInTree = BooleanArray(n)
    private val yInTree = BooleanArray(n)
    private val slack = IntArray(n)
    val xMatch = IntArray(n) { -1 }
    val yMatch = IntArray(n) { -1 }

    fun aug(y: Int) {
        var iterY = y
        while (iterY != -1) {
            val iterX = newMatchY[iterY]
            val nextY = xMatch[iterX]
            xMatch[iterX] = iterY
            yMatch[iterY] = iterX
            iterY = nextY
        }
    }

    fun bfs(xi: Int) {
        xInTree.fill(false)
        yInTree.fill(false)
        slack.fill(inf)
        stack[0] = xi
        var top = 0
        while (true) {
            while (top >= 0) {
                val x = stack[top--]
                if (xInTree[x]) continue
                xInTree[x] = true
                for (y in 0 until n) if (!yInTree[y] && graph[x][y] != -inf) {
                    val newSlack = xVal[x] + yVal[y] - graph[x][y]
                    if (newSlack < slack[y]) {
                        newMatchY[y] = x
                        slack[y] = newSlack
                        if (slack[y] == 0) {
                            yInTree[y] = true
                            if (yMatch[y] == -1) {
                                aug(y)
                                return
                            } else if (!xInTree[yMatch[y]]) {
                                stack[++top] = yMatch[y]
                            }
                        }
                    }
                }
            }
            var delta = inf
            for (y in 0 until n) if (!yInTree[y]) {
                delta = minOf(delta, slack[y])
            }
            for (i in 0 until n) {
                if (xInTree[i]) {
                    xVal[i] -= delta
                }
                if (yInTree[i]) {
                    yVal[i] += delta
                } else if (slack[i] != inf) {
                    slack[i] -= delta
                }
            }
            for (y in 0 until n) if (!yInTree[y] && slack[y] == 0) {
                yInTree[y] = true
                if (yMatch[y] == -1) {
                    aug(y)
                    return
                } else if (!xInTree[yMatch[y]]) {
                    stack[++top] = yMatch[y]
                }
            }
        }
    }

    fun match() {
        for (xi in 0 until n) if (xMatch[xi] == -1) {
            bfs(xi)
        }
    }
}