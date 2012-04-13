package pl.marpiec.socnet.service.article

import event.{CreateCommentEvent, CreateArticleEvent}
import pl.marpiec.cqrs.{DataStore, EventStore}
import pl.marpiec.socnet.model.Article
import pl.marpiec.util.UID


/**
 * @author Marcin Pieciukiewicz
 */

class ArticleCommandImpl(val eventStore: EventStore, val dataStore: DataStore) extends ArticleCommand {

  def createArticle(content: String, authorUserId: UID): UID = {
    val createArticle = new CreateArticleEvent(content, authorUserId)
    val id = UID.generate
    eventStore.addEventForNewAggregate(id, createArticle)
    id
  }

  def addComment(articleId: UID, articleVersion: Int, commentContent: String, commentAuthorUserId: UID) {
    val createComment = new CreateCommentEvent(articleId, articleVersion, commentContent, commentAuthorUserId)
    eventStore.addEvent(createComment)

  }

}
