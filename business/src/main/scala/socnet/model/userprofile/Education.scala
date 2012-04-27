package pl.marpiec.socnet.model.userprofile

import org.joda.time.LocalDate
import socnet.model.userprofile.Identifiable

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class Education extends Identifiable {
  var schoolName: String = _
  var startDateOption: Option[LocalDate] = None
  var endDateOption: Option[LocalDate] = None
  var level: String = _
  var major: String = _
}
