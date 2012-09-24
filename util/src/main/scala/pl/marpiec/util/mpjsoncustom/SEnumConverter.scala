package pl.marpiec.util.mpjsoncustom

import pl.marpiec.util.senum.SEnum
import pl.marpiec.util.mpjson.{StringIterator, JsonTypeConverter}
import java.lang.reflect.Field
import pl.marpiec.util.mpjson.deserializer.{StringDeserializer, IdentifierDeserializer}

/**
 * @author Marcin Pieciukiewicz
 */

object SEnumConverter extends JsonTypeConverter[SEnum[_]] {
  def serialize(obj: Any, jsonBuilder: StringBuilder) {
    jsonBuilder.append('"').append(obj.asInstanceOf[SEnum[_]].getName).append('"')
  }

  def deserialize(jsonIterator: StringIterator, clazz: Class[_], field: Field):SEnum[_] = {

    val enumClass = Class.forName(clazz.getName + "$")
    val en: AnyRef = companion(enumClass)

    val identifier = StringDeserializer.deserialize(jsonIterator, null, null)

    clazz.getMethod("getByName", classOf[String]).invoke(en, identifier).asInstanceOf[SEnum[_]];
  }


  def companion(clazz: Class[_])(implicit man: Manifest[SEnum[_]]): AnyRef = {
    clazz.getField("MODULE$").get(man.erasure)
  }
}
