package wiggly

import scala.io.Source
import scala.util.Try

object Main extends App {
  def resourceInfo(name: String): Unit = {
    val resource = Try(getClass.getClassLoader.getResource(name))
    val path = resource.map(_.getPath)
    val sr = Try(Source.fromResource(name).getLines().mkString("\n"))

    println(s"name: $name")
    println(s"resource: $resource")
    println(s"path: $path")
    println(s"sr: $sr")
  }

  resourceInfo("application.conf")
  resourceInfo("text.txt")
  resourceInfo("remove.txt")
}
