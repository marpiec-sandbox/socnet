package pl.marpiec.util.mpjson.gson

import com.google.gson._
import java.lang.reflect.{ParameterizedType, Type}

/**
 * @author Marcin Pieciukiewicz
 */

object OptionSerializer {
  val CLASS_PROPERTY = "c"
  val VALUE_PROPERTY = "v"
}

class OptionSerializer extends JsonSerializer[Option[Any]] with JsonDeserializer[Option[Any]] {

  def serialize(src: Option[Any], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    val jsonObject = new JsonObject
    if (src.isDefined) {
      def value = src.get
      val typeArguments = typeOfSrc.asInstanceOf[ParameterizedType].getActualTypeArguments
      if (value.asInstanceOf[Object].getClass != typeArguments(0)) {
        jsonObject.addProperty(OptionSerializer.CLASS_PROPERTY, value.asInstanceOf[Object].getClass.getName)
      }
      jsonObject.add(OptionSerializer.VALUE_PROPERTY, context.serialize(value))
      jsonObject
    } else {
      jsonObject
    }
  }


  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Option[Any] = {
    if (json.isJsonNull) {
      None
    } else if (json.isJsonObject && json.getAsJsonObject.entrySet().size() == 0) {
      None
    } else {
      val objectType = determineObjectType(json.getAsJsonObject, typeOfT)
      Option(context.deserialize(json.getAsJsonObject.get(OptionSerializer.VALUE_PROPERTY), objectType))
    }
  }

  private def determineObjectType(jsonObject: JsonObject, typeOfT: Type): Type = {
    if (jsonObject.has(OptionSerializer.CLASS_PROPERTY)) {
      Class.forName(jsonObject.get(OptionSerializer.CLASS_PROPERTY).getAsString)
    } else {
      val typeArguments = typeOfT.asInstanceOf[ParameterizedType].getActualTypeArguments
      typeArguments(0)
    }
  }

}