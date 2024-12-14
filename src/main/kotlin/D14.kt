import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.jvm.Throws
import kotlin.math.min

fun main() {
    data class Robot(var px: Int, var py: Int, var vx: Int, var vy: Int)
    run {
        val secs = 100
        val gridWidth = 101
        val gridHeight = 103
        val robotRegex = """p=([-\d]+)+,([-\d]+) v=([-\d]+),([-\d]+)""".toRegex()
        readInput("D14.txt").lineSequence()
            .map {
                val (px, py, vx, vy) = robotRegex.matchEntire(it)!!.destructured
                Robot(px.toInt(), py.toInt(), vx.toInt(), vy.toInt())
            }
            .onEach {
                it.px += it.vx * secs
                it.px = Math.floorMod(it.px, gridWidth)
                it.py += it.vy * secs
                it.py = Math.floorMod(it.py, gridHeight)
            }
            .filter {
                it.px != gridWidth / 2 && it.py != gridHeight / 2
            }
            .groupBy {
                IntVec2(min(it.px / (gridWidth / 2), 1), min(it.py / (gridHeight / 2), 1))
            }
            .map {
                it.value.size
            }
            .fold(1) { acc: Int, i: Int ->
                acc * i
            }.let {
                println(it)
            }
    }

    run {
        val gridWidth = 101
        val gridHeight = 103
        val robotRegex = """p=([-\d]+)+,([-\d]+) v=([-\d]+),([-\d]+)""".toRegex()
        val robots = readInput("D14.txt").lineSequence()
            .map {
                val (px, py, vx, vy) = robotRegex.matchEntire(it)!!.destructured
                Robot(px.toInt(), py.toInt(), vx.toInt(), vy.toInt())
            }
            .toList()

        repeat(10000) {
            val image = BufferedImage(gridWidth, gridHeight, BufferedImage.TYPE_BYTE_GRAY)
            robots.forEach {
                image.setRGB(it.px, it.py, -1)
            }
            ImageIO.write(image, "png", File("G:\\temp\\d14\\$it.png"))

            robots.forEach { robot ->
                robot.px += robot.vx
                robot.px = Math.floorMod(robot.px, gridWidth)
                robot.py += robot.vy
                robot.py = Math.floorMod(robot.py, gridHeight)
            }
        }
    }
}