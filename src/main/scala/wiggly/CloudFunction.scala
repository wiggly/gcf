package wiggly

import cats.implicits._
import cats.effect.IO
import de.killaitis.http4s.Http4sCloudFunction
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.server.Router
import cats.effect.unsafe.implicits.global
import wiggly.CloudFunction.httpApp

import java.io.{BufferedReader, File, InputStreamReader}
import scala.io.Source
import scala.util.Try

object CloudFunction {

  def pathInfo(name: String): IO[Unit] = {
    val files = Try(new File(name).listFiles.filter(_.isFile).toList.mkString(" :: "))
    IO.delay(println( s"PATH INFO '$name': $files"))
  }

  def resourceInfo(name: String): IO[Unit] = {
    val resource = Try(getClass.getClassLoader.getResource(name))
    val path = resource.map(_.getPath)
    val sr = Try(Source.fromResource(name).getLines().mkString("\n"))

    for {
      _ <-    IO.delay(println( s"name: $name"))
      _ <-    IO.delay(println( s"resource: $resource"))
      _ <-    IO.delay(println( s"path: $path"))
      _ <-    IO.delay(println( s"sr: $sr"))
    } yield ()
  }

  def logResource(name: String): IO[Unit] = {
    val resource = Try(getClass.getClassLoader.getResource(name))
    val path = resource.map(_.getPath)
    val sr = resource.flatMap(r => Try(Source.createBufferedSource(r.openStream())))



    for {
      contentStream <- IO.fromTry(sr)
      _ <- IO.delay(println(s"CONTENT\n: ${contentStream.getLines().toList.mkString("\n")}"))
    } yield ()
  }


  val httpApp = Router("/" -> HttpRoutes.of[IO] {
    case GET -> Root / "hello" =>
      for {
        _ <- pathInfo("/")
        _ <- pathInfo("src/main/resources")
        _ <- pathInfo(".")
        _ <- resourceInfo("application.conf")
        _ <- resourceInfo("test.txt")
        _ <- resourceInfo("remove.txt")

        _ <- logResource("application.conf")

        _ <- IO.delay(println("Working..."))
        result <- Ok()
      } yield result
  }).orNotFound
}

class CloudFunction extends Http4sCloudFunction(httpApp)
