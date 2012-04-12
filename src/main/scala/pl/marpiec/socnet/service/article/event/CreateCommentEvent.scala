package pl.marpiec.socnet.service.article.event

import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}
import pl.marpiec.socnet.model.article.ArticleComment
import pl.marpiec.socnet.model.Article
import org.joda.time.LocalDateTime
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

class CreateCommentEvent(entityId: UUID, expectedVersion: Int, val commentContent: String, val commentAuthorUserId: UUID)
  extends CqrsEvent(entityId, expectedVersion, classOf[Article]) {

  def applyEvent(entity: CqrsEntity) {
    val article = entity.asInstanceOf[Article]
    val comment = new ArticleComment(commentContent, new LocalDateTime, commentAuthorUserId)
    article.comments += comment
  }
}
