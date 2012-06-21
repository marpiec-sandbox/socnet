package pl.marpiec.util

import org.testng.annotations.Test
import org.joda.time.LocalDate
import org.testng.Assert._

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
  var e:Long = _
  var f:Float = _
  var g:Double = _
}

class AnotherBeanParent {
  var d:Int = _
  var e:String = _
}
class AnotherBean extends AnotherBeanParent {
  var a:Int = _
  var b:String = _
  var c:List[String] = _
}


@Test
class BeanUtilTest {


  def testSimpleCopying() {

    val bean: SimpleBean = createSimpleBean

    val beanCopy = BeanUtil.copyProperties(new SimpleBean, bean)

    assertTrue(beanCopy ne bean)
    assertEquals(beanCopy.a, bean.a)
    assertEquals(beanCopy.b, bean.b)
    assertEquals(beanCopy.c, bean.c)
    assertEquals(beanCopy.d, bean.d)
    assertEquals(beanCopy.e, bean.e)
    assertEquals(beanCopy.f, bean.f)
    assertEquals(beanCopy.g, bean.g)

  }

  def testCopyBetweenDifferentTypes() {
    val bean: SimpleBean = createSimpleBean

    val anotherBean = BeanUtil.copyProperties(new AnotherBean, bean)

    assertTrue(anotherBean ne bean)
    assertEquals(anotherBean.a, bean.a)
    assertEquals(anotherBean.b, bean.b)
    assertNull(anotherBean.c)
    assertEquals(anotherBean.d, bean.d)
    assertNull(anotherBean.e)
  }

  def testClear() {
    val bean: SimpleBean = createSimpleBean

    BeanUtil.clearProperties(bean)

    assertEquals(bean.a, 0)
    assertEquals(bean.b, "")
    assertEquals(bean.c, null)
    assertEquals(bean.d, 0)
    assertEquals(bean.e, 0)
    assertEquals(bean.f, 0.0f)
    assertEquals(bean.g, 0.0)

  }


  private def createSimpleBean: SimpleBean = {
    val bean = new SimpleBean
    bean.a = 5
    bean.b = "Aaa"
    bean.c = new LocalDate(2012, 5, 5)
    bean.d = 10
    bean.e = 5L
    bean.f = 1.3f
    bean.g = 2.4
    bean
  }
}
