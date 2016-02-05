import org.scalatest._

import org.mdedetrich.utforsca.SealedContents


object SealedContentsSpec {
  sealed abstract class ADT1(val id:Long)
  case object A extends ADT1(1)
  case object B extends ADT1(2)
  case object C extends ADT1(3)

  object ADT1 {
    val all:Set[ADT1] = SealedContents.values[ADT1]
  }

}

class SealedContentsSpec extends FlatSpec {
  "SealedContents" should "have its size equal to sum of all of its child values" in {
    assert(SealedContentsSpec.ADT1.all.size == 3)
  }

  it should "return its ID's when looked up" in {
    val a = SealedContentsSpec.ADT1.all.find(_.id == 1)
    assert(a == Some(SealedContentsSpec.A))

    val b = SealedContentsSpec.ADT1.all.find(_.id == 2)
    assert(b == Some(SealedContentsSpec.B))

    val c = SealedContentsSpec.ADT1.all.find(_.id == 3)
    assert(c == Some(SealedContentsSpec.C))

    val none = SealedContentsSpec.ADT1.all.find(_.id == 4)
    assert(none == None)
  }

  it should "equal manually constructed version of itself" in {
    val constructed = Set(SealedContentsSpec.A,SealedContentsSpec.B,SealedContentsSpec.C)
    assert(SealedContentsSpec.ADT1.all == constructed)
  }

}
