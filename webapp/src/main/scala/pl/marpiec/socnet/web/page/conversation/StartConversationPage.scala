package pl.marpiec.socnet.web.page.conversation

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.UserDatabase
import pl.marpiec.util.UID
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException

/**
 * @author Marcin Pieciukiewicz
 */

object StartConversationPage {
  val USER_ID_PARAM = "userId"
}

class StartConversationPage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  //dependencies
  @SpringBean
  private var userDatabase: UserDatabase = _

  //get data
  val userId = UID.parseOrZero(parameters.get(StartConversationPage.USER_ID_PARAM).toString)

  val userOption = userDatabase.getUserById(userId)

  if (userOption.isEmpty) {
    throw new AbortWithHttpErrorCodeException(404);
  }
  val user = userOption.get


  add(new Label("contactFullName", user.fullName))
}
