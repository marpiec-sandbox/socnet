package pl.marpiec.socnet.web.page.profile.editUserProfilePage.model

import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.model.AbstractReadOnlyModel
import pl.marpiec.socnet.constant.Month

/**
 * @author Marcin Pieciukiewicz
 */

class JobExperienceDateIModel(val jobExperience: JobExperience) extends AbstractReadOnlyModel[String] {
  def getObject: String = {
    val from = formatDate(jobExperience.fromYear, jobExperience.fromMonthOption)
    if (jobExperience.currentJob) {
      from
    } else {
      val to = formatDate(jobExperience.toYear, jobExperience.toMonthOption)
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