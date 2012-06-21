package pl.marpiec.socnet.service.article.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.article.ArticleComment
import pl.marpiec.socnet.model.Article
import org.joda.time.LocalDateTime
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class CreateCommentEvent(val commentContent: String, val commentAuthorUserId: UID) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val article = aggregate.asInstanceOf[Article]
    val comment = new ArticleComment(commentContent, new LocalDateTime, commentAuthorUserId)
    article.comments += comment
  }

  def entityClass = classOf[Article]
}
