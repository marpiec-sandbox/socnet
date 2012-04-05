package pl.marpiec.socnet.database

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.socnet.model.Article
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class ArticleDatabaseMockImplTest {
  def testBasicDatabaseOperations() {
    val articleDatabase:ArticleDatabase = new ArticleDatabaseMockImpl
    
    val article = new Article
    article.id = 1
    article.content = "tresc artykulu"
    article.authorUserId = 1
    article.creationTime = new LocalDateTime

    articleDatabase.addArticle(article)
    val articleOption = articleDatabase.getArticleById(1)

    assertTrue(articleOption.isDefined)
    val articleFromDatabase = articleOption.get

    assertTrue(article ne articleFromDatabase)
    assertEquals(articleFromDatabase.content, article.content)
    assertEquals(articleFromDatabase.creationTime, article.creationTime)

  }
}
