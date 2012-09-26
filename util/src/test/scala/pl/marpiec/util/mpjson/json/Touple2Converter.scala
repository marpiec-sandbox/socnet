package pl.marpiec.util.json

import java.lang.reflect.Type
import com.google.gson._


/**
 * @author Marcin Pieciukiewicz
 */

class Touple2Converter extends JsonSerializer[Tuple2[_, _]] with JsonDeserializer[Tuple2[_, _]] {
  def serialize(src: (_, _), typeOfSrc: Type, context: JsonSerializationContext) = {
    val array = new JsonArray()
    array.add(new JsonPrimitive(src._1.toString))
    array.add(new JsonPrimitive(src._2.toString))
    array
  }

  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) = {
    val array = json.asInstanceOf[JsonArray]
    (array.get(0).getAsString, array.get(1).getAsString)
  }
}
