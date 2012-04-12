package pl.marpiec.socnet.model

import org.testng.Assert._
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class ArticleTest {
  val article = new Article
  article.id = 1
  article.version = 1
  article.content = "Tresc artykulu"
  article.authorUserId = 1
  article.creationTime = new LocalDateTime()

  val articleCopy = article.copy.asInstanceOf[Article]

  assertTrue(articleCopy ne article)

  assertEquals(articleCopy.id, article.id)
  assertEquals(articleCopy.version, article.version)
  assertEquals(articleCopy.content, article.content)
  assertEquals(articleCopy.authorUserId, article.authorUserId)
  assertEquals(articleCopy.creationTime, article.creationTime)
}
