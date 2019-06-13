package client

import java.io.File

import org.scalatest.FunSuite

class DataLoaderTest extends FunSuite {

  test("testLoadData") {
    for (elem <- DataLoader.loadData(DataLoader.listFiles(new File("./src/test/scala/resources/")))) println(elem.toString())
  }

  test("testListFiles") {
    for (elem <- DataLoader.listFiles(new File("./src/test/scala/resources/test_data.json"))) assert(elem.toString === "./src/test/scala/resources/test_data.json")

  }
  test("testLoadJsonData") {
    for (elem <- DataLoader.loadJsonData(new File("./src/test/scala/resources/test_data.json"))) assert(elem.toString() === "{\"temperature\":12,\"last_update\":\"12-01-2019\"}")
  }

  test("testLoadCsvData") {
    for (elem <- DataLoader.loadJsonData(new File("./src/test/scala/resources/test_data.csv"))) assert(elem.toString() === "1,Perceuse,15,consomme peu")
  }
}
