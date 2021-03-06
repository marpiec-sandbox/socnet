package pl.marpiec.socnet.readdatabase.nosql

import pl.marpiec.socnet.model.Article
import pl.marpiec.util.UID
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.mongodb.DatabaseConnectorImpl
import pl.marpiec.cqrs.{Aggregate, DataStoreListener, DataStore}
import pl.marpiec.socnet.readdatabase.ArticleDatabase


/**
 * @author Marcin Pieciukiewicz
 */

@Repository("articleDatabase")
class ArticleDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener[Article] with ArticleDatabase {

  val connector = new DatabaseConnectorImpl("articles")

  startListeningToDataStore(dataStore, classOf[Article])

  def addArticle(article: Article) {
    connector.insertAggregate(article)
  }

  def updateArticle(article: Article) {
    connector.insertAggregate(article)
  }

  def getArticleById(id: UID): Option[Article] = connector.getAggregateById(id, classOf[Article])

  def getAllArticles = connector.getAllAggregates(classOf[Article])

  def onEntityChanged(article: Article) {
    connector.insertAggregate(article)
  }
}
