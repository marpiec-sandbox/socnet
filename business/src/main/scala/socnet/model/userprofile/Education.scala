package pl.marpiec.socnet.model.userprofile

import org.joda.time.LocalDate

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class Education {
  var schoolName: String = _
  var startDateOption: Option[LocalDate] = None
  var endDateOption: Option[LocalDate] = None
  var level: String = _
  var major: String = _
}
