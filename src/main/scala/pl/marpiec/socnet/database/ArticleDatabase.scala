package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.Article
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

trait ArticleDatabase {
  def addArticle(article:Article)

  def updateArticle(article:Article)

  def getArticleById(id: UUID):Option[Article]
  
  def getAllArticles:List[Article]
}
