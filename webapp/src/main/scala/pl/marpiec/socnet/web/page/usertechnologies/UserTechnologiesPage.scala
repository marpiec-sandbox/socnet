package pl.marpiec.socnet.web.page.usertechnologies

import scala.collection.JavaConversions._
import model.AddTechnologyFormModel
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.ProgrammerProfileDatabase
import pl.marpiec.socnet.service.programmerprofile.ProgrammerProfileCommand
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.constant.TechnologyKnowledgeLevel
import org.apache.wicket.markup.html.form.{ChoiceRenderer, DropDownChoice, TextField}
import org.apache.wicket.Component
import pl.marpiec.socnet.web.component.wicket.form.{OneButtonAjaxForm, StandardAjaxSecureForm}
import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Pieciukiewicz
 */

class UserTechnologiesPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var programmerProfileDatabase: ProgrammerProfileDatabase = _
  @SpringBean private var programmerProfileCommand: ProgrammerProfileCommand = _

  val programmerProfile = programmerProfileDatabase.getProgrammerProfileByUserId(session.userId).
    getOrElse(throw new IllegalStateException("No programmer profile defined for user"))

  var loadedTechnologiesList = programmerProfile.technologyKnowledge
  var addedTechnologies: Map[String, Int] = Map()
  var removedTechnologies: List[String] = List()

  addOrReplaceTechnologyList


  add(new StandardAjaxSecureForm[AddTechnologyFormModel]("addTechnologyForm") {
    def initialize = {
      this.standardCancelButton = false
      setModel(new CompoundPropertyModel[AddTechnologyFormModel](new AddTechnologyFormModel))
    }

    def buildSchema = {
      add(new TextField[String]("technologyName"))
      add(new DropDownChoice[TechnologyKnowledgeLevel]("knowledgeLevel", TechnologyKnowledgeLevel.values,
        new ChoiceRenderer[TechnologyKnowledgeLevel]("translation")))
    }

    def onSecureSubmit(target: AjaxRequestTarget, formModel: AddTechnologyFormModel) {

      val technology = formModel.technologyName
      val level = formModel.knowledgeLevel

      if(validateAddTechnologyForm(technology, level)) {
        addedTechnologies += technology -> level.value
        loadedTechnologiesList += technology -> level.value

        val technologiesList = addOrReplaceTechnologyList
        target.add(technologiesList)
      } else {
        formModel.warningMessage = "Nalezy podac nazwe technologii i poziom jej znajomosci"
        target.add(this.warningMessageLabel)
      }
    }

    def onSecureCancel(target: AjaxRequestTarget, formModel: AddTechnologyFormModel) {
      //ignore, there is no cancel button
    }
  })

  add(new OneButtonAjaxForm("saveChangesButton", "Zapisz zmiany", (target: AjaxRequestTarget) => {

    programmerProfileCommand.changeTechnologies(session.userId, programmerProfile.id, programmerProfile.version, addedTechnologies, removedTechnologies)
    setResponsePage(classOf[UserTechnologiesPage])

  }))

  private def validateAddTechnologyForm(technology: String, level: TechnologyKnowledgeLevel): Boolean = {
    StringUtils.isNotBlank(technology) && level != null &&
      level.value > TechnologyKnowledgeLevel.MIN_VALUE && level.value <= TechnologyKnowledgeLevel.MAX_VALUE
  }

  private def addOrReplaceTechnologyList: Component = {
    val technologiesList = new WebMarkupContainer("technologyList") {

      setOutputMarkupId(true)

      add(new RepeatingView("technologySummary") {
        for (technology: (String, Int) <- loadedTechnologiesList) {

          add(new AbstractItem(newChildId()) {
            val (name, level) = technology

            add(new Label("name", name))
            add(new Label("level", TechnologyKnowledgeLevel.getByValue(level).translation))

            add(new OneButtonAjaxForm("removeTechnologyButton", "Usun", (target: AjaxRequestTarget) => {

              loadedTechnologiesList -= name
              removedTechnologies ::= name

              val technologiesList = addOrReplaceTechnologyList
              target.add(technologiesList)
            }))
          })
        }
      })


    }
    UserTechnologiesPage.this.addOrReplace(technologiesList)
    technologiesList
  }

}
