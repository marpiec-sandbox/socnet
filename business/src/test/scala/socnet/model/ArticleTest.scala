package pl.marpiec.socnet.model

import org.testng.Assert._
import org.joda.time.LocalDateTime
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class ArticleTest {

  val article = new Article
  article.id = UID.generate
  article.version = 1
  article.content = "Tresc artykulu"
  article.authorUserId = UID.generate
  article.creationTime = new LocalDateTime()

  val articleCopy = article.copy.asInstanceOf[Article]

  assertTrue(articleCopy ne article)

  assertEquals(articleCopy.id, article.id)
  assertEquals(articleCopy.version, article.version)
  assertEquals(articleCopy.content, article.content)
  assertEquals(articleCopy.authorUserId, article.authorUserId)
  assertEquals(articleCopy.creationTime, article.creationTime)
}
