package pl.marpiec.socnet.web.page

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.{UserProfileDatabase, UserDatabase}
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.WebMarkupContainer
import pl.marpiec.socnet.readdatabase.UserContactsDatabase
import pl.marpiec.socnet.model.UserContacts
import pl.marpiec.socnet.web.component.contacts.PersonContactInfo
import profile.UserProfilePreviewPage

/**
 * @author Marcin Pieciukiewicz
 */

object FindPeoplePage {
  val QUERY_PARAM = "query"
}

class FindPeoplePage(parameters: PageParameters) extends SecureWebPage(SocnetRoles.USER) {

  @SpringBean
  private var userDatabase: UserDatabase = _

  @SpringBean
  private var userProfileDatabase: UserProfileDatabase = _

  @SpringBean
  private var userContactsDatabase: UserContactsDatabase = _

  val query = parameters.get(FindPeoplePage.QUERY_PARAM).toString

  val foundUsers = userDatabase.findUser(query)

  val currentUserContacts = userContactsDatabase.getUserContactsByUserId(session.userId).getOrElse(new UserContacts)

  val userProfiles = userProfileDatabase.getUserProfiles(foundUsers)


  add(new RepeatingView("foundUsers") {
    foundUsers.foreach(user => {
      add(new AbstractItem(newChildId()) {
        add(UserProfilePreviewPage.getLink(user).add(new Label("userName", user.fullName)))

        val profileOption = userProfiles.get(user)
        if (profileOption.isDefined) {
          val profile = profileOption.get
          add(new WebMarkupContainer("profile") {
            add(new Label("city", profile.city))
            add(new Label("professionalTitle", profile.professionalTitle))
          })
        } else {
          add(new WebMarkupContainer("profile").setVisible(false))
        }


        add(new PersonContactInfo("personContactInfo", user.id, currentUserContacts))


      })

    })


  })


}
