package pl.marpiec.socnet.service.article

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait ArticleCommand {

  def createArticle(content: String, authorUserId: UID):UID

  def addComment(articleId: UID, articleVersion: Int, commentContent: String, commentAuthorUserId: UID)
}
