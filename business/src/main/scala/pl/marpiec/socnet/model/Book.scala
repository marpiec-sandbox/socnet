package pl.marpiec.socnet.model

import book.BookDescription
import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.BeanUtil
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

class Book extends Aggregate(null, 0) {

  var creationTime: LocalDateTime = _
  var description = new BookDescription

  def copy: Aggregate = {
    BeanUtil.copyProperties(new Book, this)
  }

}
