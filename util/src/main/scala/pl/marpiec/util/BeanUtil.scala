package pl.marpiec.util

import java.lang.reflect.{Type, Field, Method}


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

object BeanUtil {

  private val CACHE_LOCK = new Object
  private val ZERO_INT:java.lang.Integer = 0
  private val ZERO_LONG:java.lang.Long = 0L
  private val ZERO_FLOAT:java.lang.Float = 0.0f
  private val ZERO_DOUBLE:java.lang.Double = 0.0

  private var fieldsCache = Map[Class[_], List[String]]()
  private var fieldCache = Map[(Class[_], String), Field]()
  private var gettersCache = Map[Field, Method]()
  private var settersCache = Map[Field, Method]()


  def clearProperties(bean: AnyRef) {

    val beanClass = bean.getClass

    getDeclaredFields(beanClass).foreach((fieldName:String) => {
      val field = getDeclaredField(beanClass, fieldName)
      val setter = settersCache.get(field).get
      val fieldType = field.getGenericType
      setDefaultValueIntoField(bean, setter, fieldType)
    })

  }


  private def setDefaultValueIntoField(bean: AnyRef, setter:Method, fieldType: Type): AnyRef = {
    if (fieldType == classOf[Int]) {
      setter.invoke(bean, ZERO_INT)
    } else if (fieldType == classOf[Boolean]) {
      setter.invoke(bean, java.lang.Boolean.FALSE)
    } else if (fieldType == classOf[String]) {
      setter.invoke(bean, "")
    } else if (fieldType == classOf[Long]) {
      setter.invoke(bean, ZERO_LONG)
    } else if (fieldType == classOf[Float]) {
      setter.invoke(bean, ZERO_FLOAT)
    } else if (fieldType == classOf[Double]) {
      setter.invoke(bean, ZERO_DOUBLE)
    } else if (fieldType == classOf[Option[_]]) {
      setter.invoke(bean, None)
    } else {
      setter.invoke(bean, null)
    }
  }



  def copyProperties[T <: AnyRef](toBean:T, fromBean:AnyRef):T = {

    getDeclaredFields(toBean.getClass).foreach((fieldName:String) => {
      try {
        val fromField = getDeclaredField(fromBean.getClass, fieldName)
        val toField = getDeclaredField(toBean.getClass, fieldName)

        copyValuesIfTypeMatches(toBean, toField, fromBean, fromField)
      } catch {
        case ex:NoSuchFieldException => {}
      }
    })

    toBean
  }

  private def copyValuesIfTypeMatches[T <: AnyRef](toBean: T, toField:Field, fromBean: AnyRef, fromField:Field): Any = {
    if (fromField.getGenericType == toField.getGenericType) {
      val getter = gettersCache.get(fromField).get
      val setter = settersCache.get(toField).get
      setter.invoke(toBean, getter.invoke(fromBean))
    }
  }


  private def getDeclaredFields(fromClazz:Class[_]):List[String] = {
    val fieldsOption = fieldsCache.get(fromClazz)
    if (fieldsOption.isEmpty) {
      CACHE_LOCK.synchronized {
        getFieldsAndInsertToCache(fromClazz)
      }
    } else {
      fieldsOption.get
    }
  }


  private def getFieldsAndInsertToCache(fromClazz: Class[_]): List[String] = {
    var clazz = fromClazz
    var fields = List[Field]()
    do {
      fields ++= clazz.getDeclaredFields
      clazz = clazz.getSuperclass
    } while (clazz != null)

    getFieldNamesAfterPuttingIntoCache(fromClazz, fields)
  }

  private def getFieldNamesAfterPuttingIntoCache(clazz: Class[_], fields:List[Field]): List[String] = {
    var fieldsNames = List[String]()
    fields.foreach((f: Field) => {
      fieldsNames = f.getName :: fieldsNames
    })
    fieldsCache += clazz -> fieldsNames
    fieldsNames
  }

  private def getDeclaredField(fromClazz:Class[_], name:String):Field = {
    val fieldOption = fieldCache.get((fromClazz, name))
    if (fieldOption.isEmpty) {
      CACHE_LOCK.synchronized {
        getFieldAndInsertToCache(fromClazz, name)
      }
    } else {
      fieldOption.get
    }
  }

  private def getFieldAndInsertToCache(fromClazz: Class[_], name: String): Field = {

    val (field, clazz) = findFieldInClass(name, fromClazz)

    val getter = clazz.getMethod(name)
    val setter = clazz.getMethod(name + "_$eq", field.getType)

    fieldCache += (fromClazz, name) -> field
    gettersCache += field -> getter
    settersCache += field -> setter

    field
  }

  private def findFieldInClass(name: String, fromClazz: Class[_]): (Field, Class[_]) = {
    var clazz = fromClazz
    do {
      try {
        val field = clazz.getDeclaredField(name)
        return (field, clazz)
      } catch {
        case ex: NoSuchFieldException => {/*ignore*/}
      }
      clazz = clazz.getSuperclass
    } while (clazz != null)

    throw new NoSuchFieldException()

  }


}
