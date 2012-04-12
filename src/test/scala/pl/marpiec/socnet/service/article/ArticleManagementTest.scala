package pl.marpiec.socnet.service.article

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.Article

import pl.marpiec.socnet.database.{ArticleDatabaseMockImpl, ArticleDatabase}
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class ArticleManagementTest {

  def testSimpleArticleCreationAndCommentsAdding() {
    val eventStore: EventStore = new EventStoreImpl
    val entityCache: EntityCache = new EntityCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)

    val articleCommand: ArticleCommand = new ArticleCommandImpl(eventStore, dataStore)

    val userId = UUID.randomUUID()

    val articleId = articleCommand.createArticle("Tresc artykulu", userId)

    var article = dataStore.getEntity(classOf[Article], articleId).asInstanceOf[Article]

    assertEquals(article.content, "Tresc artykulu")

    articleCommand.addComment(article.uuid, article.version, "Tresc komentarza", userId)

    article = dataStore.getEntity(classOf[Article], articleId).asInstanceOf[Article]

    assertEquals(article.comments.length, 1)
    assertEquals(article.comments.last.content, "Tresc komentarza")

  }
}
