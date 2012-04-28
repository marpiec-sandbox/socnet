package pl.marpiec.util

import org.testng.Assert._
import org.testng.annotations.Test
import org.joda.time.LocalDate

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class SimpleBean {
  var a:Int = _
  var b:String = _
  var c:LocalDate = _
}


@Test
class BeanUtilTest {

  def testSimpleCopying() {

    val bean = new SimpleBean
    bean.a = 5
    bean.b = "Aaa"
    bean.c = new LocalDate(2012,5,5)

    val beanCopy = BeanUtil.copyProperties(new SimpleBean, bean)

    assertTrue(beanCopy ne bean)
    assertEquals(beanCopy.a, bean.a)
    assertEquals(beanCopy.b, bean.b)
    assertEquals(beanCopy.c, bean.c)


  }

  def testClear() {
    val bean = new SimpleBean
    bean.a = 5
    bean.b = "Aaa"
    bean.c = new LocalDate(2012,5,5)

    BeanUtil.clearProperties(bean)

    assertEquals(bean.a, 0)
    assertEquals(bean.b, "")
    assertEquals(bean.c, null)
  }
}
