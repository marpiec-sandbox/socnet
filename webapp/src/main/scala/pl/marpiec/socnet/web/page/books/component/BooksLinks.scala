package pl.marpiec.socnet.web.page.books.component

/**
 * @author Marcin Pieciukiewicz
 */

object BooksLinks {

  val ALL_BOOKS = 1
  val YOUR_BOOKS = 2
  val ADD_BOOK = 3
  val ALL_SUGGESTIONS = 4
  val YOUR_SUGGESTIONS = 5

  val ALL_BOOKS_LINKS = YOUR_BOOKS :: ADD_BOOK :: ALL_SUGGESTIONS :: YOUR_SUGGESTIONS :: Nil
  val YOUR_BOOKS_LINKS = ALL_BOOKS :: ADD_BOOK :: ALL_SUGGESTIONS :: YOUR_SUGGESTIONS :: Nil
  val ADD_BOOK_LINKS = YOUR_BOOKS :: ALL_BOOKS :: Nil
  val ADD_BOOK_SUGGESTION_LINKS = YOUR_BOOKS :: ALL_BOOKS :: YOUR_SUGGESTIONS :: Nil
  val ALL_SUGGESTIONS_LINKS = ALL_BOOKS :: Nil
  val YOUR_SUGGESTIONS_LINKS = YOUR_BOOKS :: ALL_BOOKS :: Nil

  val BOOK_PREVIEW_LINKS = YOUR_BOOKS :: ALL_BOOKS :: Nil
  val BOOK_SUGGESTION_PREVIEW_LINKS = YOUR_BOOKS :: ALL_BOOKS :: YOUR_SUGGESTIONS :: ALL_SUGGESTIONS :: ADD_BOOK :: Nil

}
