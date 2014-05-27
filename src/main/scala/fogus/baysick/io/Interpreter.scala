package fogus.baysick.io

import com.twitter.util.Eval

/**
 * Command line interpreter for the Baysick DSL
 * @author Alexander Erben
 */
object Interpreter extends App {

  override def main(args: Array[String]) = {
    val pre = "\nimport fogus.baysick.Baysick\n\nobject BasicApplication extends Baysick { def main():() => Unit = {"
    val post = "\nreturn {() => RUN} }}"
    val builder: StringBuilder = new StringBuilder(pre)

    var line: String = ""
    Console println "Ready. Enter code now."
    do {
      line = Console readLine()
      builder.append(line).append("\n")
    } while (!line.contains("END"))
    builder.append(post)

    val env = new Eval()
    env.compile(builder.toString())
    val application = env.inPlace[() => Unit]("BasicApplication.main()")
    application.apply()
  }
}