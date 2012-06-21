package pl.marpiec.socnet.web.page.editUserProfilePage.model

import org.apache.wicket.model.AbstractReadOnlyModel
import pl.marpiec.socnet.constant.Month
import pl.marpiec.socnet.model.userprofile.Education

/**
 * @author Marcin Pieciukiewicz
 */

class EducationDateIModel(val education: Education) extends AbstractReadOnlyModel[String] {
  def getObject: String = {
    val from = formatDate(education.fromYear, education.fromMonthOption)
    if (education.stillStudying) {
      from
    } else {
      val to = formatDate(education.toYear, education.toMonthOption)
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