package pl.marpiec.socnet.web.application

import pl.marpiec.socnet.web.page._
import books._
import contacts.{InvitationsReceivedPage, InvitationsSentPage, ContactsPage}
import conversation.{ConversationPage, UserConversationsPage, StartConversationPage}
import forgotPassword.{PasswordHaveBeenChangedPage, ConfirmForgotPasswordPage, TriggerChangeForgottenPasswordPage, ForgotPasswordPage}
import profile.{UserProfilePreviewPage, EditUserProfilePage}
import registration.{ConfirmRegistrationPage, TriggerUserRegistrationPage, RegisterPage}
import usertechnologies.UserTechnologiesPage

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetBookmakablePages {

  def apply(application: SocnetApplication) {

    application.mountPage("dbupd", classOf[DbUpdatePage])


    application.mountPage("signout", classOf[SignOutPage])
    application.mountPage("new/article", classOf[NewArticlePage])
    application.mountPage("article/${" + ArticlePage.ARTICLE_ID_PARAM + "}", classOf[ArticlePage])
    application.mountPage("edit/profile", classOf[EditUserProfilePage])
    application.mountPage("profile/${" + UserProfilePreviewPage.USER_ID_PARAM + "}/${" + UserProfilePreviewPage.USER_NAME_PARAM + "}", classOf[UserProfilePreviewPage])

    application.mountPage("register", classOf[RegisterPage])
    application.mountPage("confirm/registration", classOf[ConfirmRegistrationPage])
    application.mountPage("cr/${" + TriggerUserRegistrationPage.TRIGGER_PARAM + "}", classOf[TriggerUserRegistrationPage])


    application.mountPage("forgot/password", classOf[ForgotPasswordPage])
    application.mountPage("confirm/forgot/password", classOf[ConfirmForgotPasswordPage])
    application.mountPage("cfp/${" + TriggerChangeForgottenPasswordPage.TRIGGER_PARAM + "}", classOf[TriggerChangeForgottenPasswordPage])
    application.mountPage("forgotted/password/changed", classOf[PasswordHaveBeenChangedPage])

    application.mountPage("find/people/${" + FindPeoplePage.QUERY_PARAM + "}", classOf[FindPeoplePage])

    application.mountPage("contacts", classOf[ContactsPage])
    application.mountPage("invitations/sent", classOf[InvitationsSentPage])
    application.mountPage("invitations/received", classOf[InvitationsReceivedPage])

    application.mountPage("conversations", classOf[UserConversationsPage])
    application.mountPage("conversation/${" + ConversationPage.CONVERSATION_ID_PARAM + "}", classOf[ConversationPage])
    application.mountPage("start/conversation/${" + StartConversationPage.USER_ID_PARAM + "}", classOf[StartConversationPage])

    application.mountPage("technologies", classOf[UserTechnologiesPage])

    application.mountPage("books/${" + BooksPage.QUERY_PARAM + "}", classOf[BooksPage])
    application.mountPage("add/book", classOf[AddBookPage])
    application.mountPage("book/${" + BookPreviewPage.BOOK_ID_PARAM + "}", classOf[BookPreviewPage])
    application.mountPage("your/books", classOf[YourBooksPage])
  }
}
