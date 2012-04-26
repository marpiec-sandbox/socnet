package pl.marpiec.util.json

import pl.marpiec.util.senum.SEnum
import com.google.gson._
import java.lang.reflect.Type

/**
 * @author Marcin Pieciukiewicz
 */

class SEnumTypeConverter extends JsonSerializer[SEnum[_]] with JsonDeserializer[SEnum[_]] {
  def serialize(src: SEnum[_], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    new JsonPrimitive(src.getName());
  }

  def companion(clazz: Class[_])(implicit man: Manifest[SEnum[_]]): AnyRef =
    clazz.getField("MODULE$").get(man.erasure)


  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SEnum[_] = {
    val name = typeOfT.asInstanceOf[Class[SEnum[_]]].getName
    val clazz = Class.forName(name + "$")
    val en: AnyRef = companion(clazz)
    clazz.getMethod("getByName", classOf[String]).invoke(en, json.getAsString).asInstanceOf[SEnum[_]];
  }
}
