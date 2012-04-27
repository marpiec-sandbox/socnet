package pl.marpiec.socnet.model.userprofile

import pl.marpiec.util.UID
import socnet.constant.Month
import socnet.model.userprofile.Identifiable

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class JobExperience extends Identifiable {

  var companyName: String = _
  var position: String = _

  var currentJob: Boolean = false

  var fromMonthOption: Option[Month] = None
  var fromYear: Int = _

  var toMonthOption: Option[Month] = None
  var toYear: Int = _

  var description: String = _
}
