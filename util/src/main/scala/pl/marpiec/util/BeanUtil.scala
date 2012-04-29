package pl.marpiec.util

import java.lang.reflect.Field


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

object BeanUtil {

  private var fieldsCache = Map[Class[_], List[Field]]()
  private var fieldCache = Map[(Class[_], String), Field]()

  def clearProperties(bean: AnyRef) {

    getDeclaredFields(bean.asInstanceOf[AnyRef].getClass).foreach((field:Field) => {

      val isAccessible = field.isAccessible

      if (!isAccessible) {
        field.setAccessible(true)
      }

      val t = field.getGenericType
      if (t == classOf[Int]) {
        field.set(bean, 0)
      } else if (t == classOf[Boolean]) {
        field.set(bean, false)
      } else if (t == classOf[String]) {
        field.set(bean, "")
      } else if (t == classOf[Option[_]]) {
        field.set(bean, None)
      } else {
        field.set(bean, null)
      }

      if (!isAccessible) {
        field.setAccessible(false)
      }

    })

  }

  def copyProperties[T](to:T, from:AnyRef):T = {

    getDeclaredFields(to.asInstanceOf[AnyRef].getClass).foreach((toField:Field) => {
      try {
        val fromField = getDeclaredField(from.getClass, toField.getName)
        if (fromField.getGenericType == toField.getGenericType) {
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


  private def getDeclaredFields(fromClazz:Class[_]):List[Field] = {
    val fieldsOption = fieldsCache.get(fromClazz)
    if (fieldsOption.isEmpty) {
      var clazz = fromClazz
      var fields = List[Field]()
      do {
        fields ++= clazz.getDeclaredFields
        clazz = clazz.getSuperclass
      } while (clazz!=null)
      fieldsCache += fromClazz -> fields
      fields
    } else {
      fieldsOption.get
    }
  }

  private def getDeclaredField(fromClazz:Class[_], name:String):Field = {
    val fieldOption = fieldCache.get((fromClazz, name))
    if (fieldOption.isEmpty) {
      var clazz = fromClazz
      var field: Field = null
      do {
        try {
          field = clazz.getDeclaredField(name)
        } catch {
          case ex:NoSuchFieldException => {/*ignore*/}
        }
        clazz = clazz.getSuperclass
      } while (field==null && clazz!=null)
      if (field==null) {
        throw new NoSuchFieldException()
      }
      fieldCache += (fromClazz, name) -> field
      field
    } else {
      fieldOption.get
    }
  }
}
