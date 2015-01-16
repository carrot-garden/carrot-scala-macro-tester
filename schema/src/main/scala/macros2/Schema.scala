package macros2

class BaseClass {
  def base = "base"
}

/**
 * ERROR
 * top-level class without companion can only expand either into an eponymous class or into a block consisting in eponymous companions
 */
@identity
class IdentityClass

/**
 * ERROR
 exception during macro expansion: 
 scala.MatchError: class ExpansionClass extends BaseClass 
 { <paramaccessor> val name: String = _; def <init>(name: String) = { super.<init>(); () } } 
 (of class scala.reflect.internal.Trees$ClassDef) 
 at macros2.Macros$.expansion(Macros.scala:70) 
 at sun.reflect.GeneratedMethodAccessor94.invoke(Unknown Source) 
 at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) 
 at java.lang.reflect.Method.invoke(Method.java:483) 
 at scala.reflect.macros.runtime.JavaReflectionRuntimes$JavaReflectionResolvers$$anonfun$resolveJavaReflectionRuntime$2.apply(JavaReflectionRuntimes.scala:34) 
 at scala.reflect.macros.runtime.JavaReflectionRuntimes$JavaReflectionResolvers$$anonfun$resolveJavaReflectionRuntime$2.apply(JavaReflectionRuntimes.scala:22) 
 at scala.tools.nsc.typechecker.Macros$class.macroExpandWithRuntime(Macros.scala:755)
 */
@expansion
class ExpansionClass(val name: String) extends BaseClass {

}

object Schema {

  val identity = new IdentityClass

  val expansion = new ExpansionClass("default")

  def main(args: Array[String]) = {
    
    /**
     * ERROR
     * value descriptor is not a member of macros2.ExpansionClass 
     */
    val descriptor = expansion.descriptor

    println(s"${descriptor}")

  }

}
