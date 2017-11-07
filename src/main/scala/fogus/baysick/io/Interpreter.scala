package fogus.baysick.io

import com.twitter.util.Eval

/**
 * Command line interpreter for the Baysick DSL
 * @author Alexander Erben
 */
object Interpreter extends App {

  /**
   * Uses the twitter eval runtime compiler to dynamically compiles the user entered code
   * in the context of a [[fogus.baysick.Baysick]] Object extension.
   * The compiled code is loaded into the virtual classloader of the [[com.twitter.util.Eval]] instance and then
   * used to retrieve a closure which when applied calls the
   * RUN method on the user's implementation of [[fogus.baysick.Baysick]].
   *
   * @param args currently ignored
   */
  override def main(args: Array[String]) = {
    val pre = "\nimport fogus.baysick.Baysick\n\nobject BasicApplication extends Baysick { def main():() => Unit = {\n"
    val post = "\nreturn {() => RUN} }}"
    val builder: StringBuilder = new StringBuilder(pre)

    Console println "------------------------- Ready. Enter code now. -----------------------"
    var line: String = ""
    do {
      line = scala.io.StdIn.readLine()
      builder.append(line).append("\n")
    } while (!line.contains("END"))
    builder.append(post)
    Console println "------------------------- Compiling code -------------------------------"

    val env = new Eval()
    env.compile(builder.toString()) // first compile the code and load the class
    val application = env.inPlace[() => Unit]("BasicApplication.main()") // retrieve the closure containing the RUN-call
    application.apply() // call RUN
  }
}