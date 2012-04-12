package pl.marpiec.socnet.model.userprofile

import org.joda.time.LocalDate
import java.util.UUID

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class JobExperience {
  var uuid:UUID = _
  var companyName: String = _
  var startDateOption: Option[LocalDate] = None
  var endDateOption: Option[LocalDate] = None
  var durationMonthsOption: Option[Int] = None
  var position: String = _
  var description: String = _
}
