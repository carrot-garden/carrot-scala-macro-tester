package macros1

// Enable macros.
import scala.language.experimental.macros

// Must use white context for structured types. 
import scala.reflect.macros.whitebox.Context

// 
import scala.annotation.StaticAnnotation

// Macro term annotation.
class inject(tree: Any) extends StaticAnnotation

// Public type for private class.
trait Parent {
  def inherited: Long
}

// Anonymous type providers
// http://docs.scala-lang.org/overviews/macros/typeproviders.html#anonymous-type-providers
object Macros {

  def instance: Any = macro makeInstance

  def makeInstance(c: Context) = {
    c.universe.reify[Any] {

      // Private class.
      class Internal extends Parent {

        // Reflection access.
        def bust: Long = System.currentTimeMillis

        // Direct access. Macro term = code embedding at the target site.
        def boom: Long = macro macroBoom

        // Direct access with annotated injection. Macro term = code embedding at the target site.
        @inject(42)
        def body: Long = macro macroBody

        // Direct access. Override can not be a macro term.
        override def inherited: Long = 123L
      }

      // Private class made visible as structured type.
      // Macro terms are not present in the generated type,
      // but are embedded at the target call macro expansion site.
      new Internal

    }
  }

  // Macro term = code embedding at the macro expansion site.
  def macroBoom(c: Context): c.Expr[Long] = {
    import c.universe._
    val tree = q"""{
      val base = 123L 
      val result = base + System.currentTimeMillis
      result  
    }"""
    c.Expr[Long](tree)
  }

  // Discover value with annotated injection.
  // Macro term = code embedding at the macro expansion site.
  def macroBody(c: Context): c.Expr[Long] = {
    val tree = c.macroApplication.symbol.annotations
      .filter(_.tree.tpe <:< c.typeOf[inject])
      .head.tree.children.tail.head
    c.Expr[Long](tree)
  }

}
