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
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import socnet.readdatabase.UserContactsDatabase
import socnet.model.UserContacts
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget

/**
 * @author Marcin Pieciukiewicz
 */

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

        if(currentUserContacts.isContact(user.id)) {
          add(new Fragment("inviteActionPanel", "userIsContact", FindPeoplePage.this))
        } else {
          add(new Fragment("inviteActionPanel", "inviteContact", FindPeoplePage.this) {
            add(new AjaxLink[String]("inviteLink") {
              def onClick(target: AjaxRequestTarget) {

              }
            })
          })
        }

        add(new WebMarkupContainer("inviteFormPanel").setVisible(false))


      })

    })


  })


}

object FindPeoplePage {
  val QUERY_PARAM = "q"
}
