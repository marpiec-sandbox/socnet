package pl.marpiec.util

/**
 * @author Marcin Pieciukiewicz
 */

object TemplateUtil {
  def fillTemplate(template:String, properties:Map[String, String]):String = {

    var temp = template
    
    for ((key, value) <- properties) {
      temp = temp.replace("#"+key+"#", value)
    }

    temp
  }
}
