package pl.marpiec.socnet.constant

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetRoles {
  val USER = "user"
  val ARTICLE_AUTHOR = "articleAuthor"
  val BOOK_EDITOR = "bookEditor"

  val NO_ROLES_REQUIRED = Array[String]()

  val DEFAULT_ROLES = Set(USER)
}
