package pl.marpiec.socnet.model.userprofile

import pl.marpiec.socnet.constant.Month

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class Education extends Identifiable {

  var schoolName: String = _
  var level: String = _
  var faculty: String = _
  var major: String = _

  var fromYear: Int = _
  var fromMonthOption: Option[Month] = None
  var toYear: Int = _
  var toMonthOption: Option[Month] = None

  var stillStudying: Boolean = _

  var description: String = _
}
