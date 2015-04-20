package com.mdedetrich.utforsca

import language.experimental.macros
import scala.reflect.macros._

object AsMap {
  implicit class Mappable[M](val model: M) extends AnyVal {
    def asMap: Map[String, Any] = macro Macros.asMap_impl[M]
  }

  private object Macros {

    def asMap_impl[T: c.WeakTypeTag](c: Context) = {
      import c.universe._

      val mapApply = Select(reify(Map).tree, newTermName("apply"))
      val model = Select(c.prefix.tree, newTermName("model"))

      val pairs = weakTypeOf[T].declarations.collect {
        case m: MethodSymbol if m.isCaseAccessor =>
          val name = c.literal(m.name.decodedName.toString)
          val value = c.Expr(Select(model, m.name))
          reify(name.splice -> value.splice).tree
      }

      c.Expr[Map[String, Any]](Apply(mapApply, pairs.toList))
    }
  }
}