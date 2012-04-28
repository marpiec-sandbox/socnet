package pl.marpiec.socnet.web.page.editUserProfilePage.model

import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.model.AbstractReadOnlyModel
import socnet.constant.Month

/**
 * @author Marcin Pieciukiewicz
 */

class MonthYearDateIModel(val jobExperience: JobExperience) extends AbstractReadOnlyModel[String] {
  def getObject: String = {
    val from = formatDate(jobExperience.fromYear, jobExperience.fromMonthOption)
    if (jobExperience.currentJob) {
      "od " + from
    } else {
      val to = formatDate(jobExperience.toYear, jobExperience.toMonthOption)
      "od " + from + " do " + to
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