package pl.marpiec.socnet.model.userprofile

import org.joda.time.LocalDate

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class JobExperience {
  var companyName: String = _
  var startDate: Option[LocalDate] = None
  var endDateOption: Option[LocalDate] = None
  var position: String = _
  var description: String = _
}
