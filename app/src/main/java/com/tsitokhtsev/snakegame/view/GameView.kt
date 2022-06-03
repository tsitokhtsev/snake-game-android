package com.tsitokhtsev.snakegame.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import com.tsitokhtsev.snakegame.R
import com.tsitokhtsev.snakegame.model.Cell
import com.tsitokhtsev.snakegame.model.CellType
import com.tsitokhtsev.snakegame.model.Direction
import java.util.*

class GameView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val mapSize: Int = 20
    private val startX: Int = 5
    private val startY: Int = 10
    private val cells: Array<Array<Cell>> =
        Array(mapSize) { i -> Array(mapSize) { j -> Cell(j, i) } }
    private val snake: LinkedList<Cell> = LinkedList<Cell>()

    private var direction: Direction = Direction.RIGHT
    private var cellSize: Int = 0
    private var cellMargin: Int = 0
    private var paint: Paint = Paint()

    fun init() {
        this.setWillNotDraw(false)

        cellSize = context.resources.getDimensionPixelSize(R.dimen.map_size) / mapSize
        cellMargin = cellSize / mapSize
        initMap()
    }

    fun setDirection(newDirection: Direction) {
        if (
            ((direction == Direction.LEFT || direction == Direction.RIGHT) &&
                    (newDirection == Direction.LEFT || newDirection == Direction.RIGHT)) ||
            ((direction == Direction.UP || direction == Direction.DOWN) &&
                    (newDirection == Direction.UP || newDirection == Direction.DOWN))
        ) {
            return
        }

        direction = newDirection
    }

    fun randomApple() {
        val random = Random()

        while (true) {
            val cell: Cell = getCell(random.nextInt(mapSize), random.nextInt(mapSize))

            if (cell.type === CellType.EMPTY) {
                cell.type = CellType.APPLE
                break
            }
        }
    }

    fun getSnake(): LinkedList<Cell> {
        return snake
    }

    fun getNext(cell: Cell): Cell {
        var x = cell.x
        var y = cell.y

        when (direction) {
            Direction.UP -> y = if (y == 0) mapSize - 1 else y - 1
            Direction.DOWN -> y = if (y == mapSize - 1) 0 else y + 1
            Direction.LEFT -> x = if (x == 0) mapSize - 1 else x - 1
            Direction.RIGHT -> x = if (x == mapSize - 1) 0 else x + 1
        }

        return getCell(x, y)
    }

    private fun getCell(x: Int, y: Int): Cell {
        return cells[y][x]
    }

    private fun initMap() {
        for (i in 0 until mapSize) {
            for (j in 0 until mapSize) {
                cells[i][j] = Cell(j, i)
            }
        }

        snake.clear()

        for (i in 0..2) {
            val cell: Cell = getCell(startX + i, startY)
            cell.type = CellType.SNAKE
            snake.addFirst(cell)
        }

        randomApple()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (y in 0 until mapSize) {
            for (x in 0 until mapSize) {
                var top = cellSize * y
                var bottom = top + cellSize
                var left = cellSize * x
                var right = left + cellSize

                when (getCell(x, y).type) {
                    CellType.APPLE -> {
                        paint.color = Color.RED
                        top += cellMargin
                        bottom -= cellMargin
                        left += cellMargin
                        right -= cellMargin
                    }
                    CellType.SNAKE -> {
                        paint.color = Color.WHITE
                        top += cellMargin
                        bottom -= cellMargin
                        left += cellMargin
                        right -= cellMargin
                    }
                    CellType.EMPTY -> paint.color = Color.BLACK
                }

                canvas?.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    paint
                )
            }
        }
    }
}