package pl.marpiec.socnet.service.article.event

import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}
import pl.marpiec.socnet.model.{ArticleComment, Article, User}
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class CreateCommentEvent(entityId:Int, expectedVersion:Int, val commentContent: String, val commentAuthorUserId:Int)
  extends CqrsEvent(entityId, expectedVersion, classOf[Article]){
  
  def applyEvent(entity: CqrsEntity) {
    val article = entity.asInstanceOf[Article]
    val comment = new ArticleComment(commentContent, LocalDateTime.now(), commentAuthorUserId)
    article.comments += comment
  }
}
