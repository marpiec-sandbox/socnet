package pl.marpiec.socnet.readdatabase.mock

import pl.marpiec.cqrs.DataStore
import pl.marpiec.socnet.model.Article
import pl.marpiec.util.UID
import org.springframework.beans.factory.annotation.Autowired
import pl.marpiec.socnet.readdatabase.ArticleDatabase


/**
 * @author Marcin Pieciukiewicz
 */


class ArticleDatabaseMockImpl @Autowired()(dataStore: DataStore)
  extends AbstractDatabase[Article](dataStore) with ArticleDatabase {

  startListeningToDataStore(dataStore, classOf[Article])

  def addArticle(article: Article) {
    add(article)
  }

  def updateArticle(article: Article) {
    addOrUpdate(article)
  }

  def getArticleById(id: UID): Option[Article] = getById(id)

  def getAllArticles: List[Article] = getAll
}
