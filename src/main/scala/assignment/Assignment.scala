package assignment

import java.util.concurrent.Executors

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Random

object Assignment {

  val POOL_SIZE = 4
  val THREADS = 10
  // deadline to wait for threads
  val WAIT_TIMEOUT = 15.seconds

  // 1 Part of Assignement
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
      .map(x => x._1)
      .reverse

  // 2 Part of Assignement
  // 2.1 Ported to Scala

  // asynchronousOperation executes operationDone function with random delay(1000)
  // Given Javascript version was tested in https://eng1003.monash/playground/ and
  // it always executes operationDone in sequential correct order for some reason.
  /** run should perform three asynchronous operations and then print
    * “OK” when all operations are finished. However, the current implementation prints “OK”
    * three times.
    */
  def asynchronousOperation(operationDone: Unit) = {
    Thread.sleep(Random.nextInt(1000))
    operationDone
  }

  // operationDone emulates some job to be done
  def operationDone(f: Unit) = f

  def run = {
    // customize the execution context to use default
    implicit lazy val ec = ExecutionContext.global

    val tasks = for (i <- 0 until 3) yield Future {
      asynchronousOperation(operationDone(println("OK: " + i)));
    }
  }

  // 2.2 Ensure that “OK” is only printed once, after ​all ​three operations are finished.
  /** runWait perform three asynchronous operations and then print
    * “OK” when all operations are finished.
    */
  def runWait = {
    // customize the execution context to use default
    implicit lazy val ec = ExecutionContext.global

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
  def runN(threads: Integer) = {
    // customize the execution context to use default
    implicit lazy val ec = ExecutionContext.global

    val tasks = for (i <- 0 until threads) yield Future {
      asynchronousOperation(operationDone(println("OK: " + i)))
    }

    Await.result(Future.sequence(tasks), 15.seconds)
    operationDone("Done")
  }

  // 2.4 Refactor the code to handle N operations, while making sure that no more than M are
  // running at the same time.
  /** runNWithPool perform N asynchronous operations using ThreadPool and then print
    * “OK” when all operations are finished.
    */
  def runNWithPool(threads: Integer, poolSize: Integer) = {
    // customize the execution context to use the specified number of threads
    implicit lazy val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(poolSize))

    val tasks = for (i <- 0 until threads) yield Future {
      asynchronousOperation(operationDone(println("OK: " + i)))
    }

    Await.result(Future.sequence(tasks), 15.seconds)
    operationDone("Done")
  }

  def main(args: Array[String]): Unit = {
    while (true) {
      //run
      runNWithPool(THREADS, POOL_SIZE)
      //runN(THREADS)
      printf("Finished %s asynchronous operations\n", THREADS)
      Thread.sleep(Random.nextInt(1000))
    }
  }
}
