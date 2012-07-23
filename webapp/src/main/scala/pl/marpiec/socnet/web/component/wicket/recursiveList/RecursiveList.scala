package pl.marpiec.socnet.web.component.wicket.recursiveList

import org.apache.wicket.markup.html.panel.{EmptyPanel, Fragment, Panel}
import org.apache.wicket.{MarkupContainer, Component}

/**
 * @author Marcin Pieciukiewicz
 */

class RecursiveList(id:String) extends Panel(id) {

  setOutputMarkupId(true)

  var emptyPanel = new EmptyPanel("container").setOutputMarkupId(true)
  add(emptyPanel)
  
  var lastLevelContainer:MarkupContainer = this

  def addElementAndReturnContainer(element:Component):Component = {

    emptyPanel = new EmptyPanel("container").setOutputMarkupId(true)

    val newContainer = new Fragment("container", "elementAndContainer", this).
      add(element).
      add(emptyPanel)

    lastLevelContainer.addOrReplace(newContainer)

    lastLevelContainer = newContainer

    val containerToRerender = lastLevelContainer

    containerToRerender
  }
  
}
