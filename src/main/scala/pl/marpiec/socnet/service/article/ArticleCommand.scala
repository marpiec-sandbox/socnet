package pl.marpiec.socnet.service.article

/**
 * @author Marcin Pieciukiewicz
 */

trait ArticleCommand {

  def createArticle(content: String, authorUserId: Int):Int

  def addComment(articleId: Int, articleVersion: Int, commentContent: String, commentAuthorUserId: Int)
}
