package pl.marpiec.socnet.web.page.userProfile

import pl.marpiec.socnet.web.application.SocnetSession
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.database.UserProfileDatabase
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.model.UserProfile
import org.apache.wicket.markup.html.{WebMarkupContainer, WebPage}
import org.apache.wicket.markup.html.form.{TextField, Form}
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.CompoundPropertyModel

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class EditUserProfilePage extends WebPage {

  val session: SocnetSession = getSession.asInstanceOf[SocnetSession]

  val userProfileDatabase: UserProfileDatabase = Factory.userProfileDatabase

  val userProfileOption = userProfileDatabase.getUserProfileByUserId(session.user.id)

  val userProfile = userProfileOption.getOrElse(new UserProfile)

  userProfile.professionalTitle = "Programista"

  var edit = false

  val panel: WebMarkupContainer = new WebMarkupContainer("personalSummaryPanel") {

    setOutputMarkupId(true)

    add(new WebMarkupContainer("personalSummaryPreview") {


      add(new Label("professionalTitle", userProfile.professionalTitle))
      add(new Label("city", userProfile.city))
      add(new Label("province", userProfile.province))
      add(new Label("wwwPage", userProfile.wwwPage))
      add(new Label("blogPage", userProfile.blogPage))

      add(new AjaxFallbackLink("editButton") {
        def onClick(target: AjaxRequestTarget) {
          edit = true
          target.add(panel)
        }
      })

      override def onConfigure() {
        setVisible(!edit)
      }
    })

    add(new Form[PersonalSummaryFormModel]("personalSummaryForm") {

      setModel(new CompoundPropertyModel[PersonalSummaryFormModel](new PersonalSummaryFormModel))

      add(new TextField[String]("professionalTitle"))
      add(new TextField[String]("city"))
      add(new TextField[String]("province"))
      add(new TextField[String]("wwwPage"))
      add(new TextField[String]("blogPage"))

      add(new Label("userName", session.user.name))

      override def onConfigure() {
        setVisible(edit)
      }
    })


  }


  add(panel);


}
