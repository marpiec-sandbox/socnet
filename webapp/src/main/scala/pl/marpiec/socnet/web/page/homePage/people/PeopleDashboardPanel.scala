package pl.marpiec.socnet.web.page.homePage.people

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.util.ValidationResult
import pl.marpiec.util.validation.PasswordValidator
import pl.marpiec.socnet.web.page.forgotPassword.PasswordHaveBeenChangedPage
import org.apache.wicket.markup.html.form.{TextField, PasswordTextField, StatelessForm}
import org.apache.commons.lang.StringUtils
import pl.marpiec.socnet.web.page.{FindPeoplePage, HomePage}
import org.apache.wicket.request.mapper.parameter.PageParameters

/**
 * @author Marcin Pieciukiewicz
 */

class PeopleDashboardPanel(id:String) extends Panel(id){

  add(new StatelessForm[StatelessForm[_]]("findPeopleForm") {
    var query: String = _

    setModel(new CompoundPropertyModel[StatelessForm[_]](this.asInstanceOf[StatelessForm[_]]))

    add(new TextField[String]("query"))

    override def onSubmit() {
      if (StringUtils.isNotEmpty(query)) {
        setResponsePage(classOf[FindPeoplePage], new PageParameters().add(FindPeoplePage.QUERY_PARAM, query))
      }
    }
  })

}
