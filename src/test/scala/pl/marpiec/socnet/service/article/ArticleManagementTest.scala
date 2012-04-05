package pl.marpiec.socnet.service.article

import org.testng.annotations.Test
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.Article
import org.testng.Assert._
import pl.marpiec.socnet.database.{ArticleDatabaseMockImpl, ArticleDatabase}

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class ArticleManagementTest {

  def testSimpleArticleCreationAndCommentsAdding() {
    val eventStore: EventStore = new EventStoreImpl
    val entityCache: EntityCache = new EntityCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val articleDatabase: ArticleDatabase = new ArticleDatabaseMockImpl

    val articleCommand: ArticleCommand = new ArticleCommandImpl(eventStore, dataStore, articleDatabase)


    val articleId = articleCommand.createArticle("Tresc artykulu", 1)

    var article = dataStore.getEntity(classOf[Article], articleId).asInstanceOf[Article]

    assertEquals(article.content, "Tresc artykulu")

    articleCommand.addComment(article.id, article.version, "Tresc komentarza", 1)

    article = dataStore.getEntity(classOf[Article], articleId).asInstanceOf[Article]

    assertEquals(article.comments.length, 1)
    assertEquals(article.comments.last.content, "Tresc komentarza")

  }
}
