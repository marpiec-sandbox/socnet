package pl.marpiec.util.mpjson

import org.testng.Assert._
import org.testng.annotations.Test
import pl.marpiec.util.{JsonSerializer, UID}

/**
 * @author Marcin Pieciukiewicz
 */
class UIDContainer{
  var id:UID = _
}

@Test
class MPJsonCustomConvertersTest {

  def testUIDSerialization {

    val uidContainer = new UIDContainer
    uidContainer.id = new UID(12345L)

    val serializer = new JsonSerializer

    val json = serializer.toJson(uidContainer)

    assertEquals(json, "{id:12345}")

    val deserialized = serializer.fromJson(json, classOf[UIDContainer])

    assertEquals(deserialized.id, uidContainer.id)

  }
  
}
