package pl.marpiec.socnet.database

import pl.marpiec.cqrs.DataStore
import pl.marpiec.socnet.model.Article
import pl.marpiec.util.UID


/**
 * @author Marcin Pieciukiewicz
 */

class ArticleDatabaseMockImpl(dataStore: DataStore) extends AbstractDatabase[Article](dataStore) with ArticleDatabase {

  startListeningToDataStore(dataStore, classOf[Article])

  def addArticle(article: Article) = add(article)

  def updateArticle(article: Article) = addOrUpdate(article)

  def getArticleById(id: UID) = getById(id)

  def getAllArticles = getAll
}
