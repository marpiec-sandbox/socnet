package pl.marpiec.util

import collection.immutable.List

/**
 * @author Marcin Pieciukiewicz
 */

object SearchUtils {
  
  def queryToWordsList(query: String): List[String] = prepareIndex(Nil ++ query.split("[^A-Za-z0-9ążśźęćńłóĄŻŚŹĘĆŃŁÓ]+"))

  def prepareIndex(index: List[String]): List[String] = {
    var properSet = Set[String]()
    index.foreach(word => {
      if(word.length() >= 3) {
        properSet += removeUnnecessaryChars(removePolishCharsFromLowercaseWord(StringFormattingUtil.toLowerCase(word)))
      }
    })
    properSet.toList
  }
  
  private def removePolishCharsFromLowercaseWord(word:String):String = {
    word.replace('ą', 'a')
      .replace('ż', 'z')
      .replace('ź', 'z')
      .replace('ś', 's')
      .replace('ę', 'e')
      .replace('ć', 'c')
      .replace('ń', 'n')
      .replace('ł', 'l')
      .replace('ó', 'o')
  }
  
  private def removeUnnecessaryChars(word:String):String = {
    word.replaceAll("[^a-z0-9]+", "")
  }

}
