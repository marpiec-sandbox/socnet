package pl.marpiec.socnet.database

import org.testng.annotations.Test
import org.testng.Assert._
import org.joda.time.LocalDateTime
import pl.marpiec.cqrs._
import pl.marpiec.socnet.service.user.event.RegisterUserEvent
import pl.marpiec.socnet.model.{User, Article}
import pl.marpiec.socnet.service.article.event.CreateArticleEvent
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class ArticleDatabaseMockImplTest {
  def testBasicDatabaseOperations() {

    val eventStore:EventStore = new EventStoreImpl
    val entityCache:EntityCache = new EntityCacheSimpleImpl
    val dataStore:DataStore = new DataStoreImpl(eventStore, entityCache)
    val articleDatabase:ArticleDatabase = new ArticleDatabaseMockImpl(dataStore)
    
    val articleId = UUID.randomUUID()
    val userId = UUID.randomUUID()
    
    val article = new Article
    article.uuid = articleId
    article.content = "tresc artykulu"
    article.authorUserId = userId
    article.creationTime = new LocalDateTime

    articleDatabase.addArticle(article)
    val articleOption = articleDatabase.getArticleById(articleId)

    assertTrue(articleOption.isDefined)
    val articleFromDatabase = articleOption.get

    assertTrue(article ne articleFromDatabase)
    assertEquals(articleFromDatabase.content, article.content)
    assertEquals(articleFromDatabase.creationTime, article.creationTime)

  }

  def testDataStoreListening() {
    val eventStore:EventStore = new EventStoreImpl
    val entityCache:EntityCache = new EntityCacheSimpleImpl
    val dataStore:DataStore = new DataStoreImpl(eventStore, entityCache)
    val articleDatabase: ArticleDatabase = new ArticleDatabaseMockImpl(dataStore)

    val userId = UUID.randomUUID()
    val articleId = UUID.randomUUID()
    eventStore.addEventForNewAggregate(articleId, new CreateArticleEvent("Tresc artykulu", userId))

    val article: Option[Article] = articleDatabase.getArticleById(articleId)

    assertTrue(article.isDefined, "Read Database not updated after event occured")

  }
}
