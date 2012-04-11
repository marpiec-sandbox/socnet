package pl.marpiec.socnet.service.article

import event.{CreateCommentEvent, CreateArticleEvent}
import pl.marpiec.cqrs.{DataStore, EventStore}
import pl.marpiec.socnet.model.Article


/**
 * @author Marcin Pieciukiewicz
 */

class ArticleCommandImpl(val eventStore: EventStore, val dataStore: DataStore) extends ArticleCommand {

  def createArticle(content: String, authorUserId: Int): Int = {
    val createArticle = new CreateArticleEvent(content, authorUserId)
    val id = eventStore.addEventForNewAggregate(createArticle)
    id
  }

  def addComment(articleId: Int, articleVersion: Int, commentContent: String, commentAuthorUserId: Int) {
    val createComment = new CreateCommentEvent(articleId, articleVersion, commentContent, commentAuthorUserId)
    eventStore.addEvent(createComment)

  }


  def loadArticleFromStore(id: Int): Article = {
    dataStore.getEntity(classOf[Article], id).asInstanceOf[Article]
  }


}
