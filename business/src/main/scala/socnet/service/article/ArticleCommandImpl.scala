package pl.marpiec.socnet.service.article

import event.{CreateCommentEvent, CreateArticleEvent}
import pl.marpiec.util.UID
import org.joda.time.LocalDateTime
import pl.marpiec.cqrs.{DatabaseEvent, UidGenerator, DataStore, EventStore}


/**
 * @author Marcin Pieciukiewicz
 */

class ArticleCommandImpl(val eventStore: EventStore, val dataStore: DataStore, val uidGenerator: UidGenerator) extends ArticleCommand {

  def createArticle(userId: UID, content: String, authorUserId: UID): UID = {
    val createArticle = new CreateArticleEvent(content, new LocalDateTime, authorUserId)
    val id = uidGenerator.nextUid
    eventStore.addEventForNewAggregate(id, new DatabaseEvent(userId, id, 0, createArticle))
    id
  }

  def addComment(userId: UID, articleId: UID, articleVersion: Int, commentContent: String, commentAuthorUserId: UID) {
    val createComment = new CreateCommentEvent(commentContent, commentAuthorUserId)
    eventStore.addEvent(new DatabaseEvent(userId, articleId, articleVersion, createComment))
  }

}
