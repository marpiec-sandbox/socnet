package pl.marpiec.socnet.service.article

import event.{CreateCommentEvent, CreateArticleEvent}
import pl.marpiec.cqrs.{DataStore, EventStore}
import pl.marpiec.socnet.model.Article
import java.util.UUID


/**
 * @author Marcin Pieciukiewicz
 */

class ArticleCommandImpl(val eventStore: EventStore, val dataStore: DataStore) extends ArticleCommand {

  def createArticle(content: String, authorUserId: UUID): UUID = {
    val createArticle = new CreateArticleEvent(content, authorUserId)
    val uuid = UUID.randomUUID()
    eventStore.addEventForNewAggregate(uuid, createArticle)
    uuid
  }

  def addComment(articleId: UUID, articleVersion: Int, commentContent: String, commentAuthorUserId: UUID) {
    val createComment = new CreateCommentEvent(articleId, articleVersion, commentContent, commentAuthorUserId)
    eventStore.addEvent(createComment)

  }

}
