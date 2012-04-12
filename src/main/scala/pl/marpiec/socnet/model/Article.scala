package pl.marpiec.socnet.model

import article.ArticleComment
import org.joda.time.LocalDateTime
import pl.marpiec.cqrs.CqrsEntity
import collection.mutable.ListBuffer
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

class Article extends CqrsEntity(null, 0) {

  var authorUserId:UUID = _
  var content:String = _
  var comments = new ListBuffer[ArticleComment]
  var creationTime:LocalDateTime = _

  def copy:CqrsEntity = {
    val article = new Article
    article.uuid = this.uuid
    article.version = this.version
    article.content = this.content
    article.creationTime = this.creationTime
    article.authorUserId = this.authorUserId
    article.comments = this.comments.clone
    //TODO zrobic lepsze kopiowanie komentarzy
    article
  }
}
