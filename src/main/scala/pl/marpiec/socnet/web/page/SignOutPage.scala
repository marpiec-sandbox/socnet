package pl.marpiec.socnet.web.page

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.RestartResponseAtInterceptPageException

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
