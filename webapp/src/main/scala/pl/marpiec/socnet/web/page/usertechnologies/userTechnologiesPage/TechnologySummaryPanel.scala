package pl.marpiec.socnet.web.page.usertechnologies.userTechnologiesPage

import scala.collection.JavaConversions._
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model
import org.apache.wicket.markup.html.form.{ChoiceRenderer, DropDownChoice}
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import pl.marpiec.socnet.constant.{TechnologyCurrentUsage, TechnologyLikeLevel, TechnologyKnowledgeLevel}
import pl.marpiec.socnet.web.component.wicket.form.OneButtonAjaxForm
import pl.marpiec.socnet.model.programmerprofile.KnownTechnology
import org.apache.wicket.{Component, AttributeModifier}
import pl.marpiec.socnet.web.page.usertechnologies.UserTechnologiesPage

/**
 * @author Marcin Pieciukiewicz
 */

class TechnologySummaryPanel(id: String, technology: (String, KnownTechnology),
                             val parent: UserTechnologiesPage, changedByDefault:Boolean) extends Panel(id) {

  val technologySummary = this
  val (name, knownTechnology) = technology

  add(new AttributeModifier("class", "technologySummary level" + knownTechnology.knowledgeLevel.value.toString + valueOrEmpty(changedByDefault, " changed")))
  add(new Label("name", name))
  add(new Label("level", knownTechnology.knowledgeLevel.value.toString))


  add(new DropDownChoice[TechnologyKnowledgeLevel]("knowledgeLevel",
    new Model[TechnologyKnowledgeLevel](knownTechnology.knowledgeLevel),
    TechnologyKnowledgeLevel.values,
    new ChoiceRenderer[TechnologyKnowledgeLevel]("translation")).add(new AjaxFormComponentUpdatingBehavior("onchange") {


    def onUpdate(target: AjaxRequestTarget) {
      knownTechnology.knowledgeLevel = this.getFormComponent.getModel.getObject.asInstanceOf[TechnologyKnowledgeLevel]
      parent.addAddedOrChangedTechnology(knownTechnology)
      parent.showSaveButtonAndAddToTargetIfNecessary(target)
    }
  }))

  add(new DropDownChoice[TechnologyLikeLevel]("technologyLikeLevel",
    new Model[TechnologyLikeLevel](knownTechnology.likeLevelOption.getOrElse(null)), TechnologyLikeLevel.values,
    new ChoiceRenderer[TechnologyLikeLevel]("translation")).add(new AjaxFormComponentUpdatingBehavior("onchange") {
    def onUpdate(target: AjaxRequestTarget) {
      knownTechnology.likeLevelOption = Option(this.getFormComponent.getModel.getObject.asInstanceOf[TechnologyLikeLevel])
      parent.addAddedOrChangedTechnology(knownTechnology)
      parent.showSaveButtonAndAddToTargetIfNecessary(target)

    }
  }))

  add(new DropDownChoice[TechnologyCurrentUsage]("technologyCurrentUsage",
    new Model[TechnologyCurrentUsage](knownTechnology.currentUsageOption.getOrElse(null)), TechnologyCurrentUsage.values,
    new ChoiceRenderer[TechnologyCurrentUsage]("translation")).add(new AjaxFormComponentUpdatingBehavior("onchange") {
    def onUpdate(target: AjaxRequestTarget) {
      knownTechnology.currentUsageOption = Option(this.getFormComponent.getModel.getObject.asInstanceOf[TechnologyCurrentUsage])
      parent.addAddedOrChangedTechnology(knownTechnology)
      parent.showSaveButtonAndAddToTargetIfNecessary(target)
    }
  }))

  add(new OneButtonAjaxForm("removeTechnologyButton", "OK", (target: AjaxRequestTarget) => {

    parent.correctTechnologyListsAfterRemoval(name)

    parent.showSaveButtonAndAddToTargetIfNecessary(target)
  }))
  
  private def valueOrEmpty(flag:Boolean, value:String):String = {
    if(flag) {
      value
    } else {
      ""
    }
  }
}
