package pl.marpiec.util

import org.testng.Assert._
import org.testng.annotations.Test
import org.joda.time.LocalDate

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class SimpleBeanParent {
  var d:Int = _
}

class SimpleBean extends SimpleBeanParent {
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
    bean.d = 10

    val beanCopy = BeanUtil.copyProperties(new SimpleBean, bean)

    assertTrue(beanCopy ne bean)
    assertEquals(beanCopy.a, bean.a)
    assertEquals(beanCopy.b, bean.b)
    assertEquals(beanCopy.c, bean.c)
    assertEquals(beanCopy.d, bean.d)

  }

  def testClear() {
    val bean = new SimpleBean
    bean.a = 5
    bean.b = "Aaa"
    bean.c = new LocalDate(2012,5,5)
    bean.d = 10

    BeanUtil.clearProperties(bean)

    assertEquals(bean.a, 0)
    assertEquals(bean.b, "")
    assertEquals(bean.c, null)
    assertEquals(bean.d, 0)
  }
}
