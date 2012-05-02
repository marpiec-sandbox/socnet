package pl.marpiec.util


/**
 * @author Marcin Pieciukiewicz
 */

object FileReadUtil {
  def readClasspathFile(fileName:String):String = {
    io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(fileName)).mkString
  }
}
