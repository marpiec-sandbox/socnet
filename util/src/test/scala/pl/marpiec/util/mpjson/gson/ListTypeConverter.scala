package pl.marpiec.util.mpjson.gson

import com.google.gson._
import java.lang.reflect.{ParameterizedType, Type}


/**
 * @author Marcin Pieciukiewicz
 */

object ListTypeConverter {
  val CLASS_PROPERTY = "c"
  val VALUE_PROPERTY = "v"
}

class ListTypeConverter extends JsonSerializer[List[_]] with JsonDeserializer[List[_]] {
  def serialize(src: List[_], typeOfSrc: Type, context: JsonSerializationContext):JsonElement = {
    val typeArguments = typeOfSrc.asInstanceOf[ParameterizedType].getActualTypeArguments
    if (src.nonEmpty && src.head.asInstanceOf[Object].getClass != typeArguments(0)) {
      //if its not simple determined it's written as touple of type and array
      val jsonObject = new JsonObject
      jsonObject.addProperty(ListTypeConverter.CLASS_PROPERTY, src.head.asInstanceOf[Object].getClass.getName)
      jsonObject.add(ListTypeConverter.VALUE_PROPERTY, context.serialize(src.toArray))
      jsonObject
    } else {
      //if type can be determined it's written as simple array
      context.serialize(src.toArray)
    }
  }

  def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext):List[_] = {
    
    var array:Array[Any] = null
    var list:List[_] = Nil

    if (json.isJsonArray) { //if type can be determined its simple array
      array = context.deserialize(json, classOf[Array[Any]])
    } else { //if it's not simple determined its touple of type and array
      val objectType = determineObjectType(json.getAsJsonObject, typeOfT)
      val jsonArray = json.getAsJsonObject.getAsJsonArray(ListTypeConverter.VALUE_PROPERTY)
      val size = jsonArray.size()
      array = new Array[Any](size)
      for (p <- 0 until size) {
        array.update(p, context.deserialize(jsonArray.get(p), objectType))
      }
    }

    array.foreach(element => {
      list =  element :: list
    })
    list.reverse

  }
  
  private def determineObjectType(jsonObject: JsonObject, typeOfT: Type): Type = {
    if (jsonObject.has(ListTypeConverter.CLASS_PROPERTY)) {
      Class.forName(jsonObject.get(ListTypeConverter.CLASS_PROPERTY).getAsString)
    } else {
      val typeArguments = typeOfT.asInstanceOf[ParameterizedType].getActualTypeArguments
      typeArguments(0)
    }
  }
}
