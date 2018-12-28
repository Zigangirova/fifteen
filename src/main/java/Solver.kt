import java.lang.Math.abs

fun Matrix<Int>.swap(a: Cell, b: Cell) {
    if (abs(a.column - b.column) + abs(a.row - b.row) != 1) throw IllegalStateException()
    val element = this[a]
    this[a] = this[b]
    this[b] = element
}

fun Matrix<Int>.find(element: Int): Cell {
    for (row in 0 until height)
        for (column in 0 until width)
            if (element == this[row, column]) return Cell(row, column)
    throw IllegalStateException()
}

fun fifteenGameSolution(matrix: Matrix<Int>): List<Int> {
    var gameSolution = matrix.sortRow(1)
    gameSolution += matrix.sortRow(2)
    gameSolution += matrix.sortColumn(1)
    gameSolution += matrix.sortColumn(2)
    gameSolution += matrix.sortTwoLowerRows()
    return gameSolution
}

fun Matrix<Int>.sortRow(row: Int): List<Int> {
    var listOfMoves = this.sortThreeFirstElements(row)
    listOfMoves += this.insertForthElement(row)
    return listOfMoves
}

fun Matrix<Int>.sortThreeFirstElements(row: Int): List<Int> {
    var listForOutput = listOf<Int>()
    for (i in 1..3) {
        val element = (row - 1) * 4 + i
        val destination = Cell(element / 4, element % 4 - 1)
        listForOutput += this.moveElement(element, destination)
    }
    return listForOutput
}

fun Matrix<Int>.moveElement(element: Int, destination: Cell): List<Int> {
    var elStart = this.find(element)
    if (elStart == destination) return emptyList()
    var listForOutput = this.moveElementInRow(elStart, destination)
    elStart = Cell(elStart.row, destination.column)
    listForOutput += this.moveElementInColumn(elStart, destination)
    return listForOutput
}

fun Matrix<Int>.moveZeroToElement(elStart: Cell, elEnd: Cell): List<Int> {
    val zeroStart = this.find(0)
    val zeroEnd = when {
        elStart.column > elEnd.column -> Cell(elStart.row, elStart.column - 1)
        elStart.column < elEnd.column -> Cell(elStart.row, elStart.column + 1)
        elStart.row > elEnd.row -> Cell(elStart.row - 1, elStart.column)
        else -> Cell(elStart.row + 1, elStart.column)
    }
    if (zeroStart == zeroEnd) return emptyList()
    return this.moveZero(zeroStart, zeroEnd, elStart)
}

fun Matrix<Int>.moveZero(zeroStart: Cell, zeroEnd: Cell, untouchable: Cell): List<Int> {
    if (zeroStart == zeroEnd) return emptyList()
    var listForOutput = emptyList<Int>()
    if (zeroStart.row < zeroEnd.row) {
        listForOutput += this.moveZeroInColumn(zeroStart, zeroEnd, untouchable)
        listForOutput += this.moveZeroInRow(this.find(0), zeroEnd, untouchable)
    } else {
        listForOutput += this.moveZeroInRow(zeroStart, zeroEnd, untouchable)
        listForOutput += this.moveZeroInColumn(this.find(0), zeroEnd, untouchable)
    }
    return listForOutput.toList()
}

fun Matrix<Int>.moveZeroInRow(zeroStart: Cell, zeroEnd: Cell, untouchable: Cell = Cell(0, 0)): List<Int> {
    if (zeroStart.column == zeroEnd.column) return emptyList()
    val listForOutput = mutableListOf<Int>()
    var currentZeroCell = zeroStart
    while (currentZeroCell.column != zeroEnd.column) {
        val columnStep = if (zeroStart.column < zeroEnd.column) 1 else -1
        val nextCell = Cell(currentZeroCell.row, currentZeroCell.column + columnStep)
        if (nextCell == untouchable) {
            listForOutput.addAll(this.bypassElementByZeroInRow(currentZeroCell, zeroEnd, columnStep))
            break
        }
        listForOutput.add(this[nextCell])
        this.swap(currentZeroCell, nextCell)
        currentZeroCell = nextCell
    }
    return listForOutput.toList()
}

fun Matrix<Int>.moveZeroInColumn(zeroStart: Cell, zeroEnd: Cell, untouchable: Cell = Cell(0, 0)): List<Int> {
    if (zeroStart.row == zeroEnd.row) return emptyList()
    val listForOutput = mutableListOf<Int>()
    var currentZeroCell = zeroStart
    while (currentZeroCell.row != zeroEnd.row) {
        val rowStep = if (zeroStart.row < zeroEnd.row) 1 else -1
        val nextCell = Cell(currentZeroCell.row + rowStep, currentZeroCell.column)
        if (nextCell == untouchable) {
            listForOutput.addAll(this.bypassElementByZeroInColumn(currentZeroCell, zeroEnd, rowStep))
            break
        }
        listForOutput.add(this[nextCell])
        this.swap(currentZeroCell, nextCell)
        currentZeroCell = nextCell
    }
    return listForOutput.toList()
}

fun Matrix<Int>.bypassElementByZeroInRow(zeroStart: Cell, zeroEnd: Cell, columnStep: Int): List<Int> {
    if (zeroStart == zeroEnd) return emptyList()
    val listForOutput = mutableListOf<Int>()
    var currentCell = zeroStart
    val movesAmt = if (zeroEnd.column == zeroStart.column + columnStep) 2 else 4
    for (move in 1..movesAmt) {
        val nextCell = when (move) {
            1 -> if (zeroStart.row == 3) Cell(zeroStart.row - 1, zeroStart.column)
            else Cell(zeroStart.row + 1, zeroStart.column)
            in 2..3 -> Cell(currentCell.row, currentCell.column + columnStep)
            else -> zeroEnd
        }
        listForOutput.add(this[nextCell])
        this.swap(currentCell, nextCell)
        currentCell = nextCell
    }
    return listForOutput.toList()
}

fun Matrix<Int>.bypassElementByZeroInColumn(zeroStart: Cell, zeroEnd: Cell, rowStep: Int): List<Int> {
    if (zeroStart == zeroEnd) return emptyList()
    val listForOutput = mutableListOf<Int>()
    var currentCell = zeroStart
    val movesAmt = if (zeroEnd.row == zeroStart.row + rowStep) 2 else 4
    for (move in 1..movesAmt) {
        val nextCell = when (move) {
            1 -> if (zeroStart.column == 3) Cell(zeroStart.row, zeroStart.column - 1)
            else Cell(zeroStart.row, zeroStart.column + 1)
            in 2..3 -> Cell(currentCell.row + rowStep, currentCell.column)
            else -> zeroEnd
        }
        listForOutput.add(this[nextCell])
        this.swap(currentCell, nextCell)
        currentCell = nextCell
    }
    return listForOutput.toList()
}

fun Matrix<Int>.moveElementInRow(elStart: Cell, elEnd: Cell): List<Int> {
    if (elStart.column == elEnd.column) return emptyList()
    val listForOutput = this.moveZeroToElement(elStart, elEnd).toMutableList()
    val columnStep = if (elStart.column < elEnd.column) 1 else -1
    var zeroCell = this.find(0)
    var currentCell = elStart
    val movesAmt = abs(elEnd.column - elStart.column)
    var nextCell = Cell(currentCell.row, currentCell.column + columnStep)
    for (move in 1..movesAmt) {
        listForOutput.addAll(this.bypassElementByZeroInRow(zeroCell, nextCell, columnStep))
        zeroCell = nextCell
        listForOutput.add(this[currentCell])
        this.swap(zeroCell, currentCell)
        zeroCell = currentCell
        currentCell = nextCell
        nextCell = Cell(currentCell.row, currentCell.column + columnStep)
    }
    return listForOutput.toList()
}

fun Matrix<Int>.moveElementInColumn(elStart: Cell, elEnd: Cell): List<Int> {
    if (elStart.row == elEnd.row) return emptyList()
    val listForOutput = this.moveZeroToElement(elStart, elEnd).toMutableList()
    val rowStep = if (elStart.row < elEnd.row) 1 else -1
    var zeroCell = this.find(0)
    var currentCell = elStart
    val movesAmt = abs(elEnd.row - elStart.row)
    var nextCell = Cell(currentCell.row + rowStep, currentCell.column)
    for (move in 1..movesAmt) {
        listForOutput.addAll(this.bypassElementByZeroInColumn(zeroCell, nextCell, rowStep))
        zeroCell = nextCell
        listForOutput.add(this[currentCell])
        this.swap(zeroCell, currentCell)
        zeroCell = currentCell
        currentCell = nextCell
        nextCell = Cell(currentCell.row + rowStep, currentCell.column)
    }
    return listForOutput.toList()
}

fun Matrix<Int>.insertForthElement(row: Int): List<Int> {
    val element = 4 * row
    if (element == this[row - 1, 3]) return emptyList()
    if (this.find(0) == Cell(row - 1, 3) && find(element) == Cell(row, 3)) {
        this.swap(Cell(row - 1, 3), Cell(row, 3))
        return listOf(element)
    }
    val destination = Cell(row, 2)
    var listOfMoves = this.moveElement(element, destination)
    listOfMoves += this.moveZero(this.find(0), Cell(row, 1), destination)
    val insertion = listOf(this[row - 1, 1], this[row - 1, 2], this[row, 2], this[row, 3],
            this[row - 1, 3], this[row, 2], this[row - 1, 2], this[row - 1, 1])
    this.swap(Cell(row, 2), Cell(row, 3))
    this.swap(Cell(row, 3), Cell(row - 1, 3))
    return listOfMoves + insertion
}

fun Matrix<Int>.sortColumn(column: Int): List<Int> {
    val firstElem = if (column == 1) 11 else 12
    val secondElem = if (column == 1) 10 else 9
    val cellForFirst = Cell(3, column - 1)
    val cellForSecond = Cell(3, column + 1)
    if (this[2, column - 1] == firstElem && this[3, column - 1] == secondElem) return emptyList()
    var listForOutput = this.moveElement(firstElem, cellForFirst)
    listForOutput += this.moveElement(secondElem, cellForSecond)
    if (this[3, column - 1] != firstElem) {
        listForOutput += this.moveZero(find(0), Cell(3, 1), cellForSecond)
        listForOutput += this.moveElement(firstElem, cellForFirst)
    }
    val zeroCell = find(0)
    val untouchable = if (zeroCell.column > column) cellForSecond else cellForFirst
    listForOutput += this.moveZero(zeroCell, Cell(3, column), untouchable)
    listForOutput += this.moveElementInRow(cellForSecond, cellForFirst)
    return listForOutput
}

fun Matrix<Int>.sortTwoLowerRows(): List<Int> {
    var listForOutput = this.moveElement(13, Cell(3, 2))
    listForOutput += this.moveZero(find(0), Cell(2, 2), Cell(3, 2))
    listForOutput += this.moveZeroInRow(Cell(2, 2), Cell(2, 0))
    listForOutput += 10
    this.swap(Cell(2, 0), Cell(3, 0))
    listForOutput += this.moveZeroInRow(Cell(3, 0), Cell(3, 3))
    listForOutput += this[2, 3]
    this.swap(Cell(3, 3), Cell(2, 3))
    listForOutput += this.moveZeroInRow(Cell(2, 3), Cell(2, 0))
    listForOutput += 9
    this.swap(Cell(2, 0), Cell(3, 0))
    listForOutput += this.moveZeroInRow(Cell(3, 0), Cell(3, 3))
    return listForOutput
}