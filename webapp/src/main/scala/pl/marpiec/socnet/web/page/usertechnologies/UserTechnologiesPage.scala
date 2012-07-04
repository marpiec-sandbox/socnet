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
import pl.marpiec.socnet.web.component.wicket.form.{OneButtonAjaxForm, StandardAjaxSecureForm}
import org.apache.commons.lang.StringUtils
import pl.marpiec.socnet.model.ProgrammerProfile
import org.apache.wicket.{AttributeModifier, Component}

/**
 * @author Marcin Pieciukiewicz
 */

class UserTechnologiesPage extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean private var programmerProfileDatabase: ProgrammerProfileDatabase = _
  @SpringBean private var programmerProfileCommand: ProgrammerProfileCommand = _

  //load data

  val programmerProfile = getProgrammerProfileOrThrow404

  var loadedTechnologiesList = programmerProfile.technologyKnowledge
  var addedTechnologies: Map[String, Int] = Map()
  var removedTechnologies: List[String] = List()

  //build schema


  addOrReplaceTechnologyList


  val saveButton = addAndReturn(new OneButtonAjaxForm("saveChangesButton", "Zapisz zmiany", (target: AjaxRequestTarget) => {

    programmerProfileCommand.changeTechnologies(session.userId, programmerProfile.id, programmerProfile.version, addedTechnologies, removedTechnologies)
    setResponsePage(classOf[UserTechnologiesPage])

  }).setVisible(false).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true))


  add(new StandardAjaxSecureForm[AddTechnologyFormModel]("addTechnologyForm") {

    val thisForm = this

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

      if (validateAddTechnologyForm(technology, level)) {
        addedTechnologies += technology -> level.value
        loadedTechnologiesList += technology -> level.value

        formModel.clear

        val technologiesList = addOrReplaceTechnologyList
        target.add(technologiesList)
        target.add(thisForm)

        if (!saveButton.isVisible) {
          saveButton.setVisible(true)
          target.add(saveButton)
        }

      } else {
        formModel.warningMessage = "Nalezy podac nazwe technologii i poziom jej znajomosci"
        target.add(this.warningMessageLabel)
      }
    }

    def onSecureCancel(target: AjaxRequestTarget, formModel: AddTechnologyFormModel) {
      //ignore, there is no cancel button
    }
  })


  //methods

  private def getProgrammerProfileOrThrow404: ProgrammerProfile = {
    programmerProfileDatabase.getProgrammerProfileByUserId(session.userId).
      getOrElse(throw new IllegalStateException("No programmer profile defined for user"))
  }

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


            add(new AttributeModifier("class", "technologySummary level" + level));
            add(new Label("name", name))
            add(new Label("level", level.toString))

            add(new OneButtonAjaxForm("removeTechnologyButton", "OK", (target: AjaxRequestTarget) => {

              if (loadedTechnologiesList.contains(name)) {
                loadedTechnologiesList -= name
                removedTechnologies ::= name
              }
              addedTechnologies -= name

              val technologiesList = addOrReplaceTechnologyList
              target.add(technologiesList)
              if (!saveButton.isVisible) {
                saveButton.setVisible(true)
                target.add(saveButton)
              }
            }))
          })
        }
      })


    }
    UserTechnologiesPage.this.addOrReplace(technologiesList)
    technologiesList
  }

}
