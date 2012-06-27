package pl.marpiec.socnet.web.component.wicket

import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Pieciukiewicz
 */

class LabelInvisibleWhenEmpty(id:String, model:IModel[_]) extends Label(id, model) {
  override def isVisible = super.isVisible && StringUtils.isNotBlank(getDefaultModelObjectAsString)
}
