package macros1

// Enable macros with reflection.
import scala.language.experimental.macros; import scala.language.reflectiveCalls

object Schema {

  /**
   * Generated instance. Does not contain macro terms.
   *
   * package macros1;
   * public class Schema$Internal$1 implements Parent {
   * public long bust() { return System.currentTimeMillis(); }
   * public long inherited() { return 123L; }
   * }
   */
  val instance = Macros.instance

  def main(args: Array[String]) = {

    /**
     * Embedded macro term expansion. Not present in the generated instance.
     *
     * long base = 123L; long result = base + System.currentTimeMillis(); long boom = result;
     */
    val boom = instance.boom

    /**
     * Reflection access to non-macro member of the generated internal class instance.
     *
     * Parent qual1 = instance();
     * long bust = BoxesRunTime.unboxToLong((Long)reflMethod$Method1(qual1.getClass()).invoke(qual1, new Object[0]));
     */
    val bust = instance.bust

    println(s"boom ${boom}")

    println(s"bust ${bust}")

  }

}
