package pl.marpiec.socnet.web.page.editUserProfilePage.model

import pl.marpiec.socnet.model.userprofile.Education
import org.apache.wicket.model.AbstractReadOnlyModel
import socnet.constant.Month
import socnet.model.userprofile.AdditionalInfo

/**
 * @author Marcin Pieciukiewicz
 */

class AdditionalInfoDateIModel(val additionalInfo: AdditionalInfo) extends AbstractReadOnlyModel[String] {
  def getObject: String = {
    val from = formatDate(additionalInfo.fromYear, additionalInfo.fromMonthOption)
    if (additionalInfo.oneDate) {
      from
    } else {
      val to = formatDate(additionalInfo.toYear, additionalInfo.toMonthOption)
      from + " - " + to
    }
  }

  private def formatDate(year: Int, monthOption: Option[Month]): String = {
    if (monthOption.isDefined) {
      monthOption.get.translation + " " + year
    } else if (year > 0) {
      year.toString
    } else {
      ""
    }
  }
}