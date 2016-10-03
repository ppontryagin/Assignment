package assignment

import java.util.concurrent.Executors

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Random


object Assignment {

  val WAIT_TIMEOUT = 15.seconds

  // 1 Assignement
  /** Takes a list as argument and outputs a new list with the unique elements
    * of the argument list in order of frequency
    * (Listof T) -> (Listof T)
    *
    * @param l input list
    */
  def frequency[T](l: List[T]): List[T] =
    l.groupBy(el => el)
      .map { case (k, v) => k -> v.length }
      .toList
      .sortBy(_._2)
      .reverse
      .map(x => x._1)

  // 2 Assignement
  // 2.1 Ported to Scala

  // asynchronousOperation executes operationDone function with random delay(1000)
  // Given Javascript version was tested in https://eng1003.monash/playground/ and
  // it always executes operationDone in correct order for some reason.
  /** run should perform three asynchronous operations and then print
    * “OK” when all operations are finished. However, the current implementation prints “OK”
    * three times.
    */
  def asynchronousOperation(operationDone: Unit) =
  //    Future {
  {
    Thread.sleep(Random.nextInt(1000))
    operationDone
  }

  def run = {

    implicit lazy val ec = ExecutionContext.global

    def operationDone(i: String) = println("OK: " + i)

    val tasks = for (i <- 0 until 3) yield Future {
      asynchronousOperation(operationDone(i.toString));
    }
  }

  // 2.2 Ensure that “OK” is only printed once, after ​all ​three operations are finished.
  /** runWait perform three asynchronous operations and then print
    * “OK” when all operations are finished.
    */
  def runWait = {
    implicit lazy val ec = ExecutionContext.global


    def operationDone(i: String) = println("OK: " + i)

    val tasks = for (i <- 0 until 3) yield Future {
      asynchronousOperation(operationDone(i.toString));
    }

    Await.result(Future.sequence(tasks), WAIT_TIMEOUT)
    operationDone("Done")
  }

  // 2.3 Refactor the code to handle N operations.
  /** runN perform N asynchronous operations and then print
    * “OK” when all operations are finished.
    */
  def runN(parallelism: Integer) = {
    implicit lazy val ec = ExecutionContext.global


    def operationDone(i: String) = println("OK: " + i)

    val tasks = for (i <- 0 until parallelism) yield Future {
      asynchronousOperation(operationDone(i.toString))
    }

    Await.result(Future.sequence(tasks), 15.seconds)
    operationDone("Done")
  }

  // 2.4 Refactor the code to handle N operations, while making sure that no more than M are
  // running at the same time.
  /** runNWithPool perform N asynchronous operations using ThreadPool and then print
    * “OK” when all operations are finished.
    */
  def runNWithPool(parallelism: Integer, poolSize: Integer) = {
    // customize the execution context to use the specified number of threads
    implicit lazy val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(poolSize))

    def operationDone(i: String) = println("OK: " + i)

    val tasks = for (i <- 0 until parallelism) yield Future {
      asynchronousOperation(operationDone(i.toString))
    }

    Await.result(Future.sequence(tasks), 15.seconds)
    operationDone("Done")
  }


  def main(args: Array[String]): Unit = {
    while (true) {
      runNWithPool(10, 1)
      //runN(10)
      println(java.lang.Thread.activeCount())
      println("Finished 10 asynchronous operations")
      Thread.sleep(Random.nextInt(1000))

    }
  }
}
