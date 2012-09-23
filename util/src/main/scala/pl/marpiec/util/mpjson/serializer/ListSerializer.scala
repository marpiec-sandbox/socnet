package pl.marpiec.util.mpjson.serializer

import pl.marpiec.util.mpjson.SerializerFactory

object ListSerializer extends SimpleSerializer {

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
        SerializerFactory.getDeserializer(element).serialize(element, jsonBuilder)
    })


    jsonBuilder.append(']')
  }


}
