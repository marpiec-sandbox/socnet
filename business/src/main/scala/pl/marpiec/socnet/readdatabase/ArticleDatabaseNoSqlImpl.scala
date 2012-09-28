package pl.marpiec.socnet.readdatabase

import pl.marpiec.socnet.model.Article
import pl.marpiec.util.UID
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.mongodb.DatabaseConnectorImpl
import pl.marpiec.cqrs.{Aggregate, DataStoreListener, DataStore}


/**
 * @author Marcin Pieciukiewicz
 */

@Repository("articleDatabase")
class ArticleDatabaseNoSqlImpl @Autowired()(dataStore: DataStore)
  extends DataStoreListener with ArticleDatabase {

  val connector = new DatabaseConnectorImpl("articles")

  startListeningToDataStore(dataStore, classOf[Article])

  def addArticle(article: Article) {
    connector.insertAggregate(article)
  }

  def updateArticle(article: Article) {
    connector.insertAggregate(article)
  }

  def getArticleById(id: UID): Option[Article] = Option[Article](connector.getAggregateById(id, classOf[Article]))

  def getAllArticles = connector.getAllAggregates(classOf[Article]).asInstanceOf[List[Article]]

  def onEntityChanged(aggregate: Aggregate) {
    connector.insertAggregate(aggregate.asInstanceOf[Article])
  }
}
