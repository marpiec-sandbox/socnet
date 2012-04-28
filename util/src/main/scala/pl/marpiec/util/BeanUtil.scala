package pl.marpiec.util

import java.lang.reflect.Field
import javax.lang.model.`type`.PrimitiveType


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

object BeanUtil {
  def clearProperties(bean: AnyRef) {

    bean.getClass.getDeclaredFields.foreach((field:Field) => {

      val isAccessible = field.isAccessible

      if (!isAccessible) {
        field.setAccessible(true)
      }

      val t = field.getGenericType
      if (t == classOf[Int]) {
        field.set(bean, 0)
      } else if (t == classOf[Boolean]) {
        field.set(bean, 0)
      } else if (t == classOf[String]) {
        field.set(bean, "")
      } else {
        field.set(bean, null)
      }

      if (!isAccessible) {
        field.setAccessible(false)
      }

    })

  }

  def copyProperties[T](to:T, from:AnyRef):T = {

    to.asInstanceOf[AnyRef].getClass.getDeclaredFields.foreach((toField:Field) => {
      try {
        val fromField = from.getClass.getDeclaredField(toField.getName)
        if (fromField.getClass == toField.getClass) {
          var fromAccessible = fromField.isAccessible
          var toAccessible = toField.isAccessible
          if (!fromAccessible) {
            fromField.setAccessible(true)
          }
          if (!toAccessible) {
            toField.setAccessible(true)
          }
          toField.set(to, fromField.get(from))
          if (!fromAccessible) {
            fromField.setAccessible(false)
          }
          if (!toAccessible) {
            toField.setAccessible(false)
          }
        }
      } catch {
        case ex:NoSuchFieldException => {}
      }
    })

    to
  }
}
