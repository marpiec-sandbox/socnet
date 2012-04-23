package pl.marpiec.socnet.service.article

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.Article

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class ArticleManagementTest {

  def testSimpleArticleCreationAndCommentsAdding() {
    val eventStore: EventStore = new EventStoreMockImpl
    val entityCache: EntityCache = new EntityCacheSimpleImpl
    val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)
    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    val articleCommand: ArticleCommand = new ArticleCommandImpl(eventStore, dataStore, uidGenerator)

    val userId = uidGenerator.nextUid

    val articleId = articleCommand.createArticle(new UID(0), "Tresc artykulu", userId)

    var article = dataStore.getEntity(classOf[Article], articleId).asInstanceOf[Article]

    assertEquals(article.content, "Tresc artykulu")

    articleCommand.addComment(new UID(0), article.id, article.version, "Tresc komentarza", userId)

    article = dataStore.getEntity(classOf[Article], articleId).asInstanceOf[Article]

    assertEquals(article.comments.length, 1)
    assertEquals(article.comments.last.content, "Tresc komentarza")

  }
}
