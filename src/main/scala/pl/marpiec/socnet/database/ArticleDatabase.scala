package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.Article

/**
 * @author Marcin Pieciukiewicz
 */

trait ArticleDatabase {
  def addArticle(article:Article)

  def updateArticle(article:Article)

  def getArticleById(id: Int):Option[Article]
  
  def getAllArticles:List[Article]
}
