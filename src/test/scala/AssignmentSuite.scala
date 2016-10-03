package assingment

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AssignmentSuiteSuite extends FunSuite {

  import assignment.Assignment.frequency

  //frequency(['a','b','b','c','b','a'])#=>['b','a','c']

  val listEmpty = List()
  val listEmptyS = List()

  val listSimple = List('a', 'b', 'b', 'c', 'b', 'a')
  val listSimpleS = List('b', 'a', 'c')


  test("Empty list is solved") {
    assert(frequency(listEmpty) === listEmptyS)
  }

  test("Simple list is solved") {
    assert(frequency(listSimple) === listSimpleS)
  }

}
