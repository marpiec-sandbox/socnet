package pl.marpiec.socnet.service.article.event

import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}
import pl.marpiec.socnet.model.article.ArticleComment
import pl.marpiec.socnet.model.Article
import org.joda.time.LocalDateTime
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class CreateCommentEvent(entityId: UID, expectedVersion: Int, val commentContent: String, val commentAuthorUserId: UID)
  extends CqrsEvent(entityId, expectedVersion) {

  def applyEvent(entity: CqrsEntity) {
    val article = entity.asInstanceOf[Article]
    val comment = new ArticleComment(commentContent, new LocalDateTime, commentAuthorUserId)
    article.comments += comment
  }

  def entityClass = classOf[Article]
}
