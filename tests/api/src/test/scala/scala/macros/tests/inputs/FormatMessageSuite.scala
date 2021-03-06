package scala.macros.tests
package inputs

import org.junit._
import org.junit.runner._
import org.junit.runners._
import org.junit.Assert._
import scala.macros.internal.inputs._
import scala.macros.inputs._
import scala.macros.internal.prettyprinters._

@RunWith(classOf[JUnit4])
class FormatMessageSuite {
  private def test(s: String)(expected: String): Unit = {
    val content = Input.VirtualFile("input", s)
    val points = 0.to(content.chars.length).map(i => Position.Range(content, i, i))
    val actual = points.map(p => s"${p.formatMessage("error", "foo")}").mkString(EOL)
    assertEquals(expected, actual)
  }

  @Test
  def empty: Unit = {
    test("")("""
      |input:1: error: foo
      |
      |^
    """.trim.stripMargin)
  }

  @Test
  def newline: Unit = {
    test("\n")("""
      |input:1: error: foo
      |
      |^
      |input:2: error: foo
      |
      |^
    """.trim.stripMargin)
  }

  @Test
  def foo: Unit = {
    test("foo")("""
      |input:1: error: foo
      |foo
      |^
      |input:1: error: foo
      |foo
      | ^
      |input:1: error: foo
      |foo
      |  ^
      |input:1: error: foo
      |foo
      |   ^
    """.trim.stripMargin)
  }

  @Test
  def fooNewline: Unit = {
    test("foo\n")("""
      |input:1: error: foo
      |foo
      |^
      |input:1: error: foo
      |foo
      | ^
      |input:1: error: foo
      |foo
      |  ^
      |input:1: error: foo
      |foo
      |   ^
      |input:2: error: foo
      |
      |^
    """.trim.stripMargin)
  }

  @Test
  def fooNewlineBar: Unit = {
    test("foo\nbar")("""
      |input:1: error: foo
      |foo
      |^
      |input:1: error: foo
      |foo
      | ^
      |input:1: error: foo
      |foo
      |  ^
      |input:1: error: foo
      |foo
      |   ^
      |input:2: error: foo
      |bar
      |^
      |input:2: error: foo
      |bar
      | ^
      |input:2: error: foo
      |bar
      |  ^
      |input:2: error: foo
      |bar
      |   ^
    """.trim.stripMargin)
  }
}
