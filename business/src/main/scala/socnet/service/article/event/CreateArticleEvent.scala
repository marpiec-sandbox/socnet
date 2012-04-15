package pl.marpiec.socnet.service.article.event

import pl.marpiec.socnet.model.Article
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}
import org.joda.time.LocalDateTime
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class CreateArticleEvent(val content:String, val creationTime:LocalDateTime, val authorUserId:UID) extends CqrsEvent {
  def applyEvent(entity: CqrsEntity) {

    val article = entity.asInstanceOf[Article]
    article.content = content
    article.authorUserId = authorUserId
    article.creationTime = creationTime
  }

  def entityClass = classOf[Article]
}
