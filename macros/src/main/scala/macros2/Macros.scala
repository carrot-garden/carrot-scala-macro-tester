package macros2

// Enable macros.
import scala.language.experimental.macros

// Must use white context for macro annotations.
import scala.reflect.macros.whitebox.Context

import scala.annotation.StaticAnnotation

// Macro annotation.
class identity extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro Macros.identity
}

// Macro annotation.
class expansion extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro Macros.expansion
}

// Public type providers
// http://docs.scala-lang.org/overviews/macros/typeproviders.html#public-type-providers
object Macros {

  /** Report status in Eclipse UI on the annotated type. */
  private def info(c: Context)(message: String) = c.info(c.enclosingPosition, message, true)

  /** Report status in Eclipse UI on the annotated type. */
  private def abort(c: Context)(message: String) = c.abort(c.enclosingPosition, message)

  def identity(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    val inputs = annottees.map(_.tree).toList

    val (annottee, expandees) = inputs match {
      case (param: ValDef) :: (rest @ (_ :: _)) =>
        info(c)(s"Identity ValDef: ${param}")
        (param, rest)
      case (param: TypeDef) :: (rest @ (_ :: _)) =>
        info(c)(s"Identity TypeDef: ${param}")
        (param, rest)
      case (param: ClassDef) :: (rest @ (_ :: _)) =>
        info(c)(s"Identity ClassDef: ${param}")
        (EmptyTree, inputs)
      case (param: ModuleDef) :: (rest @ (_ :: _)) =>
        info(c)(s"Identity ModuleDef: ${param}")
        (EmptyTree, inputs)
      case _ =>
        info(c)(s"Identity wildcard: ${}")
        (EmptyTree, inputs)
    }

    info(c)(s"Identity result: ${(annottee, expandees)}")

    val outputs = expandees

    c.Expr[Any](Block(outputs, Literal(Constant(()))))

  }

  def expansion(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    val inputs = annottees.map(_.tree).toList

    val (annottee, expandees) = inputs match {
      case (param: ClassDef) :: (Nil) =>
        info(c)(s"Expansion ClassDef: ${param}")
        /** 
         *  ERROR
         *  too many patterns for trait api: expected 1, found 9
         *  recursive value x$4 needs type
         */
        val q"""
          $mods class $name[..$para] $ctorMods(...$args) extends { ..$previous } with ..$parents { $self => 
          ..$current 
          }
        """ = param
        val tree: Tree = q"""
          $mods class $name[..$para] $ctorMods(...$args) extends { ..$previous } with ..$parents { $self => 
          ..$current
          def descriptor: String = "descriptor" 
          }
        """
        (EmptyTree, List(tree))
      case _ =>
        abort(c)(s"Expansion can be applied only to ClassDef.")
        (EmptyTree, inputs)
    }

    info(c)(s"Expansion application: ${(annottee, expandees)}")

    val outputs = expandees

    c.Expr[Any](Block(outputs, Literal(Constant(()))))

  }

}
