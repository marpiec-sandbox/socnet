package pl.marpiec.socnet.database

import org.testng.annotations.Test
import org.testng.Assert._
import org.joda.time.LocalDateTime
import pl.marpiec.cqrs._
import pl.marpiec.socnet.service.user.event.RegisterUserEvent
import pl.marpiec.socnet.model.{User, Article}
import pl.marpiec.socnet.service.article.event.CreateArticleEvent
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class ArticleDatabaseMockImplTest {
  def testBasicDatabaseOperations() {

    val eventStore:EventStore = new EventStoreMockImpl
    val entityCache:EntityCache = new EntityCacheSimpleImpl
    val dataStore:DataStore = new DataStoreImpl(eventStore, entityCache)
    val articleDatabase:ArticleDatabase = new ArticleDatabaseMockImpl(dataStore)
    val uidGenerator:UidGenerator = new UidGeneratorMockImpl
    
    val articleId = uidGenerator.nextUid
    val userId = uidGenerator.nextUid
    
    val article = new Article
    article.id = articleId
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
    val eventStore:EventStore = new EventStoreMockImpl
    val entityCache:EntityCache = new EntityCacheSimpleImpl
    val dataStore:DataStore = new DataStoreImpl(eventStore, entityCache)
    val articleDatabase: ArticleDatabase = new ArticleDatabaseMockImpl(dataStore)
    val uidGenerator:UidGenerator = new UidGeneratorMockImpl

    val userId = uidGenerator.nextUid
    val articleId = uidGenerator.nextUid
    eventStore.addEventForNewAggregate(articleId, new CreateArticleEvent("Tresc artykulu", new LocalDateTime, userId))

    val article: Option[Article] = articleDatabase.getArticleById(articleId)

    assertTrue(article.isDefined, "Read Database not updated after event occured")

  }
}
