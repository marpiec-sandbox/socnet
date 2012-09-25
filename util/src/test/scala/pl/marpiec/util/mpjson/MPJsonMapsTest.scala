package pl.marpiec.util.mpjson

/**
 * @author Marcin Pieciukiewicz
 */
import org.testng.Assert._
import org.testng.annotations.Test
import pl.marpiec.util.JsonSerializer
import pl.marpiec.util.json.annotation.{SecondSubType, FirstSubType}

class MapElement(var intValue: Int, var stringValue: String)

class SimpleMapsObject {
  
  var simpleMap:Map[String, String] = _
  @FirstSubType(classOf[Int]) @SecondSubType(classOf[Long])
  var primitiveMap:Map[Int, Long] = _
  var objectMap:Map[MapElement, MapElement] = _
  
}

@Test
class MPJsonMapsTest {

  def testMapSerialization() {

    var smo = new SimpleMapsObject

    smo.simpleMap = Map()
    smo.simpleMap += "a" -> "Ala"
    smo.simpleMap += "k" -> "Kot"

    smo.primitiveMap = Map()
    smo.primitiveMap += 1 -> 1224
    smo.primitiveMap += 5 -> 5324

    smo.objectMap = Map()
    
    smo.objectMap += new MapElement(1, "one") -> new MapElement(100, "one hundred")
    smo.objectMap += new MapElement(5, "five") -> new MapElement(500, "five hundred")

    val json = new JsonSerializer
    val serialized = json.toJson(smo)

    assertEquals(serialized, "{simpleMap:{\"a\":\"Ala\",\"k\":\"Kot\"}," +
                              "primitiveMap:{1:1224,5:5324}," +
                              "objectMap:{" +
                                "{intValue:1,stringValue:\"one\"}:{intValue:100,stringValue:\"one hundred\"}," +
                                "{intValue:5,stringValue:\"five\"}:{intValue:500,stringValue:\"five hundred\"}" +
                              "}}")

    val smoDeserialized:SimpleMapsObject = json.fromJson(serialized, classOf[SimpleMapsObject])

    assertNotNull(smoDeserialized)

    assertEquals(smoDeserialized.simpleMap, smo.simpleMap)
    assertEquals(smoDeserialized.primitiveMap, smo.primitiveMap)
    assertEquals(smoDeserialized.objectMap.size, smo.objectMap.size)
  }

}
