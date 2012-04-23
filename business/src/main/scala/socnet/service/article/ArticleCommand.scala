package pl.marpiec.socnet.service.article

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait ArticleCommand {

  def createArticle(userId:UID, content: String, authorUserId: UID):UID

  def addComment(userId:UID, articleId: UID, articleVersion: Int, commentContent: String, commentAuthorUserId: UID)
}
