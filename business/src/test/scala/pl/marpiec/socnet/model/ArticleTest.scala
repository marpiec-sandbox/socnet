package pl.marpiec.socnet.model

import org.testng.Assert._
import org.joda.time.LocalDateTime
import pl.marpiec.cqrs.{UidGeneratorMockImpl, UidGenerator}

/**
 * @author Marcin Pieciukiewicz
 */

class ArticleTest {

  val uidGenerator:UidGenerator = new UidGeneratorMockImpl

  val article = new Article
  article.id = uidGenerator.nextUid
  article.version = 1
  article.content = "Tresc artykulu"
  article.authorUserId = uidGenerator.nextUid
  article.creationTime = new LocalDateTime()

  val articleCopy = article.copy.asInstanceOf[Article]

  assertTrue(articleCopy ne article)

  assertEquals(articleCopy.id, article.id)
  assertEquals(articleCopy.version, article.version)
  assertEquals(articleCopy.content, article.content)
  assertEquals(articleCopy.authorUserId, article.authorUserId)
  assertEquals(articleCopy.creationTime, article.creationTime)
}
