import java.io.File

object Main {
  def main(args: Array[String]): Unit = {
    //TODO: place thing in it.

    println(DataLoader.loadData(DataLoader.listFiles(new File("./src/test/scala/resources"))))
  }
}
