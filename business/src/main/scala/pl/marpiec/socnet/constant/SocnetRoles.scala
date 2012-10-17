package pl.marpiec.socnet.constant

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetRoles {
  val USER = "user"
  var TRUSTED_USER = "trustedUser"
  val ARTICLE_AUTHOR = "articleAuthor"
  val BOOK_EDITOR = "bookEditor"

  val NO_ROLES_REQUIRED = Array[String]()

  val DEFAULT_ROLES = Set(USER)
}
