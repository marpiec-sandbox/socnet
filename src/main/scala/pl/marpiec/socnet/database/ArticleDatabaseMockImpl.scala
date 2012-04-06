package pl.marpiec.socnet.database

import collection.mutable.HashMap
import pl.marpiec.socnet.service.article.exception.ArticleAlreadyExistsException
import pl.marpiec.cqrs.{CqrsEntity, DataStoreListener, DataStore}
import pl.marpiec.socnet.model.{User, Article}


/**
 * @author Marcin Pieciukiewicz
 */

class ArticleDatabaseMockImpl(dataStore:DataStore) extends DataStoreListener with ArticleDatabase {

  private val articleDatabase = new HashMap[Int, Article]

  startListeningToDataStore(dataStore, classOf[Article])

  def onEntityChanged(entity: CqrsEntity) {
    val article = entity.asInstanceOf[Article]
    if(article.version==1) {
      addArticle(article)
    } else {
      updateArticle(article)
    }
  }

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
