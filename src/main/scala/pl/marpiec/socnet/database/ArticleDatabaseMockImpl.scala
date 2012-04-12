package pl.marpiec.socnet.database

import collection.mutable.HashMap
import exception.EntryAlreadyExistsException
import pl.marpiec.cqrs.{CqrsEntity, DataStoreListener, DataStore}
import pl.marpiec.socnet.model.{User, Article}


/**
 * @author Marcin Pieciukiewicz
 */

class ArticleDatabaseMockImpl(dataStore:DataStore) extends AbstractDatabase[Article](dataStore) with ArticleDatabase {

  startListeningToDataStore(dataStore, classOf[Article])

  def addArticle(article: Article) = add(article)

  def updateArticle(article: Article) = update(article)

  def getArticleById(id: Int) = getById(id)

  def getAllArticles = getAll
}
