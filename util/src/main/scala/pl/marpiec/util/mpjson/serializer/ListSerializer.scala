package pl.marpiec.util.mpjson.serializer

import pl.marpiec.util.mpjson.{JsonTypeSerializer, SerializerFactory}


object ListSerializer extends JsonTypeSerializer {

  def serialize(obj: Any, jsonBuilder:StringBuilder) = {

    jsonBuilder.append('[')

    var isNotFirstField = false

    val list = obj.asInstanceOf[List[_]]

    list.foreach(element => {

        if(isNotFirstField) {
          jsonBuilder.append(",")
        } else {
          isNotFirstField = true
        }
        SerializerFactory.getSerializer(element).serialize(element, jsonBuilder)
    })


    jsonBuilder.append(']')
  }


}
