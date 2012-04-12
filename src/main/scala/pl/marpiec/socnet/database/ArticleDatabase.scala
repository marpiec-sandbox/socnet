package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.Article
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait ArticleDatabase {
  def addArticle(article:Article)

  def updateArticle(article:Article)

  def getArticleById(id: UID):Option[Article]
  
  def getAllArticles:List[Article]
}
