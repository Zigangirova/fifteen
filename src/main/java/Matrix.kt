
interface Matrix<E> {

    val height: Int

    val width: Int

    operator fun get(row: Int, column: Int): E
    operator fun get(cell: Cell): E

    operator fun set(row: Int, column: Int, value: E)
    operator fun set(cell: Cell, value: E)
}

fun createMatrix(height: Int, width: Int, e: Int): Matrix<Int> =
        if (height <= 0 || width <= 0) throw IllegalArgumentException()
        else MatrixImpl(width, height, e)

fun createMatrix(values: List<List<Int>>): Matrix<Int> {
    val matrix = createMatrix(values.size, values[0].size, values[0][0])
    for (row in 0 until values.size) {
        for (column in 0 until values[0].size) {
            matrix[row, column] = values[row][column]
        }
    }
    return matrix
}

class MatrixImpl(override val width: Int, override val height: Int, e: Int) : Matrix<Int> {

    private val matrix = MutableList(height) { MutableList(width) { e } }

    override fun get(row: Int, column: Int): Int  = matrix[row][column]

    override fun get(cell: Cell): Int  = matrix[cell.row][cell.column]

    override fun set(row: Int, column: Int, value: Int) {
        matrix[row][column] = value
    }

    override fun set(cell: Cell, value: Int) {
        matrix[cell.row][cell.column] = value
    }

    override fun equals(other: Any?): Boolean {
        if (other is Matrix<*> && height == other.height && width == other.width)
            for (row in 0 until height)
                for (column in 0 until width)
                    if (this[row, column] != other[row, column]) return false
        return true
    }

    override fun toString(): String {
        val str = StringBuilder("")
        for (i in 0 until height) {
            if (i != 0) str.append("\n")
            for (j in 0 until width) str.append(String.format("%2d", this[i, j]), ' ')
        }
        return str.toString()

    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + matrix.hashCode()
        return result
    }
}