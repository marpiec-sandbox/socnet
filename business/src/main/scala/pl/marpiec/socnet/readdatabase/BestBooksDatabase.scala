package pl.marpiec.socnet.readdatabase

import pl.marpiec.socnet.redundandmodel.book.BestBooks

/**
 * @author Marcin Pieciukiewicz
 */
trait BestBooksDatabase {

  def getBestBooks:BestBooks

}
