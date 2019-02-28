import java.io.File

import client.{DataLoader, DataSender}

object Main {
  def main(args: Array[String]): Unit = {

    val sender = DataSender("localhost", 8080, "bar")

    sender.sendData(DataLoader.loadData(DataLoader.listFiles(new File("./data"))))

  }
}
