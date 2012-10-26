package pl.marpiec.socnet.web.component.usertechnologies

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.ProgrammerProfileDatabase
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.util.{OrderingUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */

class UserTechnologiesPreviewPanel(id: String, userId: UID) extends Panel(id) {

  @SpringBean private var programmerProfileDatabase: ProgrammerProfileDatabase = _

  val programmerProfileOption = programmerProfileDatabase.getProgrammerProfileByUserId(userId)


  if (programmerProfileOption.isDefined && programmerProfileOption.get.technologyKnowledge.nonEmpty) {
    val programmerProfile = programmerProfileOption.get

    val technologiesNamesAlphabetic = programmerProfile.technologyKnowledge.keySet.toList.sorted(OrderingUtil.STRING_IGNORE_CASE_ORDERING)

    add(new RepeatingView("technology") {

      technologiesNamesAlphabetic.foreach(technologyName => {

        add(new AbstractItem(newChildId()) {
          add(new Label("technologyName", technologyName))
          add(new Label("knowledgeLevel", programmerProfile.technologyKnowledge(technologyName).knowledgeLevel.value.toString))
        })

      })

    })
  } else {
    setVisible(false)
  }

}
