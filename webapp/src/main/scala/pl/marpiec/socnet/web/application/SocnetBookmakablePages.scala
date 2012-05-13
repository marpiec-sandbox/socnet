package pl.marpiec.socnet.web.application

import pl.marpiec.socnet.web.page._
import forgotPassword.{PasswordHaveBeenChangedPage, ConfirmForgotPasswordPage, TriggerChangeForgottenPasswordPage, ForgotPasswordPage}
import registration.{ConfirmRegistrationPage, TriggerUserRegistrationPage, RegisterPage}

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetBookmakablePages {

  def apply(application: SocnetApplication) {

    application.mountPage("signout", classOf[SignOutPage])
    application.mountPage("new-article", classOf[NewArticlePage])
    application.mountPage("article/${"+ArticlePage.ARTICLE_ID_PARAM+"}", classOf[ArticlePage])
    application.mountPage("edit-profile", classOf[EditUserProfilePage])
    application.mountPage("profile/${"+UserProfilePreviewPage.USER_ID_PARAM+"}/${"+UserProfilePreviewPage.USER_NAME_PARAM+"}", classOf[UserProfilePreviewPage])

    application.mountPage("register", classOf[RegisterPage])
    application.mountPage("confirm-registration", classOf[ConfirmRegistrationPage])
    application.mountPage("cr/${"+TriggerUserRegistrationPage.TRIGGER_PARAM+"}", classOf[TriggerUserRegistrationPage])


    application.mountPage("forgot-password", classOf[ForgotPasswordPage])
    application.mountPage("confirm-forgot-password", classOf[ConfirmForgotPasswordPage])
    application.mountPage("cfp/${"+TriggerChangeForgottenPasswordPage.TRIGGER_PARAM+"}", classOf[TriggerChangeForgottenPasswordPage])
    application.mountPage("forgotted-password-changed", classOf[PasswordHaveBeenChangedPage])

    application.mountPage("find-people", classOf[FindPeoplePage])
  }
}
