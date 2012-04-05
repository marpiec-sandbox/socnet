package pl.marpiec.socnet.database

import pl.marpiec.socnet.model.Article
import collection.mutable.HashMap
import pl.marpiec.socnet.service.article.exception.ArticleAlreadyExistsException


/**
 * @author Marcin Pieciukiewicz
 */

class ArticleDatabaseMockImpl extends ArticleDatabase {

  private val articleDatabase = new HashMap[Int, Article]

  def addArticle(article: Article) {
    this.synchronized {
      if (articleDatabase.get(article.id).isDefined) {
        throw new ArticleAlreadyExistsException
      } else {
        val articleCopy: Article = article.createCopy
        articleDatabase += article.id -> articleCopy;
      }
    }
  }

  def updateArticle(article: Article) {
    this.synchronized {
      if (articleDatabase.get(article.id).isEmpty) {
        throw new IllegalStateException("No article defined in database, articleId=" + article.id)
      } else {
        val articleCopy: Article = article.createCopy
        articleDatabase += article.id -> articleCopy;
      }
    }
  }

  def getArticleById(id: Int): Option[Article] = {
    articleDatabase.get(id) match {
      case Some(user) => Option.apply(user.createCopy)
      case None => None
    }
  }

  def getAllArticles: List[Article] = {
    articleDatabase.values.toList
  }
}
