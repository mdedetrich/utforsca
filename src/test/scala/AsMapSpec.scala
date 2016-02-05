import org.scalatest._

import org.mdedetrich.utforsca.AsMap._
import spire.math._


object AsMapSpec {

  case class Test1(a: List[String], b: Int)

  case class TypeTest1[T](a: List[T], b: Option[T])

  case class ExistentialTypeTest[T: Numeric](a: T, b: List[T])

}

class AsMapSpec extends FlatSpec {
  "asMap" should "equal its contents extracted as a map" in {
    val a = AsMapSpec.Test1(List("a", "b"), 100)

    //Manually construct our map

    val a_Mapped = Map(
      "a" -> List("a", "b"),
      "b" -> 100
    )

    assert(a.asMap == a_Mapped)

    val b = AsMapSpec.TypeTest1(List(BigDecimal(100)), Option(BigDecimal(5)))

    val b_Mapped = Map(
      "a" -> List(BigDecimal(100)),
      "b" -> Option(BigDecimal(5))
    )

    assert(b.asMap == b_Mapped)

    val c = AsMapSpec.ExistentialTypeTest(BigDecimal(1), List(BigDecimal(199)))

    val c_Mapped = Map(
      "a" -> BigDecimal(1),
      "b" -> List(BigDecimal(199))
    )

    assert(c.asMap == c_Mapped)
  }

}