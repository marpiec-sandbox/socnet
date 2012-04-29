package pl.marpiec.util

import java.lang.reflect.{Field, Method}
import scala.Predef._
import scala.Int


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

object BeanUtil {

  private var fieldsCache = Map[Class[_], List[String]]()
  private var fieldCache = Map[(Class[_], String), Field]()
  private var gettersCache = Map[Field, Method]()
  private var settersCache = Map[Field, Method]()
  private val FIELDS_CACHE_LOCK = new Object
  private val FIELD_CACHE_LOCK = new Object
  private val ZERO:java.lang.Integer = 0

  def clearProperties(bean: AnyRef) {

    getDeclaredFields(bean.asInstanceOf[AnyRef].getClass).foreach((fieldName:String) => {

      val field = getDeclaredField(bean.asInstanceOf[AnyRef].getClass, fieldName)
      val setter = settersCache.get(field).get
      val t = field.getGenericType
      if (t == classOf[Int]) {
        setter.invoke(bean, ZERO)
      } else if (t == classOf[Boolean]) {
        setter.invoke(bean, java.lang.Boolean.FALSE)
      } else if (t == classOf[String]) {
        setter.invoke(bean, "")
      } else if (t == classOf[Option[_]]) {
        setter.invoke(bean, None)
      } else {
        setter.invoke(bean, null)
      }

    })

  }

  def copyProperties[T](to:T, from:AnyRef):T = {

    getDeclaredFields(to.asInstanceOf[AnyRef].getClass).foreach((fieldName:String) => {
      try {
        val fromField = getDeclaredField(from.getClass, fieldName)
        val toField = getDeclaredField(to.asInstanceOf[AnyRef].getClass, fieldName)
        if (fromField.getGenericType == toField.getGenericType) {
          val getter = gettersCache.get(fromField).get
          val setter = settersCache.get(toField).get
          setter.invoke(to, getter.invoke(from))
        }
      } catch {
        case ex:NoSuchFieldException => {}
      }
    })

    to
  }


  private def getDeclaredFields(fromClazz:Class[_]):List[String] = {
    val fieldsOption = fieldsCache.get(fromClazz)
    if (fieldsOption.isEmpty) {
      FIELDS_CACHE_LOCK.synchronized {
        getFieldsAndInsertToCache(fromClazz)
      }
    } else {
      fieldsOption.get
    }
  }


  def getFieldsAndInsertToCache(fromClazz: Class[_]): List[String] = {
    var clazz = fromClazz
    var fields = List[Field]()
    do {
      fields ++= clazz.getDeclaredFields
      clazz = clazz.getSuperclass
    } while (clazz != null)
    var fieldsNames = List[String]()
    fields.foreach((f:Field) => {
      fieldsNames = f.getName :: fieldsNames
    })
    fieldsCache += fromClazz -> fieldsNames
    fieldsNames
  }

  private def getDeclaredField(fromClazz:Class[_], name:String):Field = {
    val fieldOption = fieldCache.get((fromClazz, name))
    if (fieldOption.isEmpty) {
      FIELD_CACHE_LOCK.synchronized {
        getFieldAndInsertToCache(fromClazz, name)
      }
    } else {
      fieldOption.get
    }
  }

  def getFieldAndInsertToCache(fromClazz: Class[_], name: String): Field = {
    var clazz = fromClazz
    var field: Field = null
    var getter: Method = null
    var setter: Method = null
    do {
      try {
        field = clazz.getDeclaredField(name)
      } catch {
        case ex: NoSuchFieldException => {
          /*ignore*/
        }
      }
      if(field==null) {
        clazz = clazz.getSuperclass
      }
    } while (field == null && clazz != null)
    if (field == null) {
      throw new NoSuchFieldException()
    }

    getter = clazz.getMethod(name)
    setter = clazz.getMethod(name + "_$eq", field.getType)

    fieldCache += (fromClazz, name) -> field
    gettersCache += field -> getter
    settersCache += field -> setter

    field
  }


}
