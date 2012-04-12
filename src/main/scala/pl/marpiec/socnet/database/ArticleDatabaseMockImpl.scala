package pl.marpiec.socnet.database

import pl.marpiec.cqrs.DataStore
import pl.marpiec.socnet.model.Article
import java.util.UUID


/**
 * @author Marcin Pieciukiewicz
 */

class ArticleDatabaseMockImpl(dataStore: DataStore) extends AbstractDatabase[Article](dataStore) with ArticleDatabase {

  startListeningToDataStore(dataStore, classOf[Article])

  def addArticle(article: Article) = add(article)

  def updateArticle(article: Article) = update(article)

  def getArticleById(id: UUID) = getById(id)

  def getAllArticles = getAll
}
