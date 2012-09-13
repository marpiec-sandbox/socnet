package pl.marpiec.util.json

import java.lang.reflect.Type
import com.google.gson._


/**
 * @author Marcin Pieciukiewicz
 */

class ListTypeConverter extends JsonSerializer[List[_]] with JsonDeserializer[List[_]] {
  def serialize(src: List[_], typeOfSrc: Type, context: JsonSerializationContext):JsonElement = {
    context.serialize(src.toArray)
  }

  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext):List[_] = {
    val array:Array[Any] = context.deserialize(json, classOf[Array[Any]])
    var list:List[_] = Nil
    array.foreach(element => {
       list =  element :: list
    })
    list.reverse
  }
}
