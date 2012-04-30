package socnet.model.userprofile

import socnet.constant.Month

/**
 * @author Marcin Pieciukiewicz
 */

class AdditionalInfo extends Identifiable {
  var title:String = _
  var description:String = _

  var fromYear: Int = _
  var fromMonthOption: Option[Month] = None
  var toYear: Int = _
  var toMonthOption: Option[Month] = None

  var oneDate: Boolean = _
}
