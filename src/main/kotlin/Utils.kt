import java.io.File

fun readInput(fileName: String): String {
    return File("inputs/$fileName").readText().trimEnd('\n')
}