package pl.marpiec.socnet.service.article.event

import pl.marpiec.socnet.model.Article
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class CreateArticleEvent(val content:String, val authorUserId:Int) extends CqrsEvent(0, 0, classOf[Article]) {
  def applyEvent(entity: CqrsEntity) {
    val article = entity.asInstanceOf[Article]
    article.content = content
    article.authorUserId = authorUserId
    article.creationTime = new LocalDateTime
  }
}
