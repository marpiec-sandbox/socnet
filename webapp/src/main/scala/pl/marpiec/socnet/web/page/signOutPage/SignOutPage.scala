package pl.marpiec.socnet.web.page.signOutPage

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.RestartResponseAtInterceptPageException
import pl.marpiec.socnet.web.page.homePage.HomePage

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class SignOutPage(parameters: PageParameters) extends WebPage {

  getSession.invalidate()

  override def onBeforeRender {
    throw new RestartResponseAtInterceptPageException(classOf[HomePage])
  }
}
