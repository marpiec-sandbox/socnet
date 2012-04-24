package pl.marpiec.socnet.service.article.event

import pl.marpiec.socnet.model.Article
import pl.marpiec.cqrs.{Aggregate, Event}
import org.joda.time.LocalDateTime
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class CreateArticleEvent(val content:String, val creationTime:LocalDateTime, val authorUserId:UID) extends Event {
  def applyEvent(aggregate: Aggregate) {

    val article = aggregate.asInstanceOf[Article]
    article.content = content
    article.authorUserId = authorUserId
    article.creationTime = creationTime
  }

  def entityClass = classOf[Article]
}
