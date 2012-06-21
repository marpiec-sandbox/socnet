package pl.marpiec.util


/**
 * @author Marcin Pieciukiewicz
 */

object FileReadUtil {

  val defaultEncoding = "UTF-8"

  def readClasspathFile(fileName: String): String = {
    io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(fileName), defaultEncoding).mkString
  }
}
