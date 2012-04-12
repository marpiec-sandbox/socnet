package pl.marpiec.socnet.model

import org.testng.Assert._
import org.joda.time.LocalDateTime
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

class ArticleTest {

  val article = new Article
  article.uuid = UUID.randomUUID()
  article.version = 1
  article.content = "Tresc artykulu"
  article.authorUserId = UUID.randomUUID()
  article.creationTime = new LocalDateTime()

  val articleCopy = article.copy.asInstanceOf[Article]

  assertTrue(articleCopy ne article)

  assertEquals(articleCopy.uuid, article.uuid)
  assertEquals(articleCopy.version, article.version)
  assertEquals(articleCopy.content, article.content)
  assertEquals(articleCopy.authorUserId, article.authorUserId)
  assertEquals(articleCopy.creationTime, article.creationTime)
}
