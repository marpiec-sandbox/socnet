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

    application.mountPageNoVersioning("dbupd", classOf[DbUpdatePage])

    application.mountPageNoVersioning("signout", classOf[SignOutPage])
    application.mountPageNoVersioning("new/article", classOf[NewArticlePage])
    application.mountPageNoVersioning("article/${" + ArticlePage.ARTICLE_ID_PARAM + "}", classOf[ArticlePage])
    application.mountPageNoVersioning("edit/profile", classOf[EditUserProfilePage])
    application.mountPageNoVersioning("profile/${" + UserProfilePreviewPage.USER_ID_PARAM + "}/${" + UserProfilePreviewPage.USER_NAME_PARAM + "}", classOf[UserProfilePreviewPage])

    application.mountPage("register", classOf[RegisterPage])
    application.mountPage("confirm/registration", classOf[ConfirmRegistrationPage])
    application.mountPage("cr/${" + TriggerUserRegistrationPage.TRIGGER_PARAM + "}", classOf[TriggerUserRegistrationPage])


    application.mountPageNoVersioning("forgot/password", classOf[ForgotPasswordPage])
    application.mountPageNoVersioning("confirm/forgot/password", classOf[ConfirmForgotPasswordPage])
    application.mountPageNoVersioning("cfp/${" + TriggerChangeForgottenPasswordPage.TRIGGER_PARAM + "}", classOf[TriggerChangeForgottenPasswordPage])
    application.mountPageNoVersioning("forgotted/password/changed", classOf[PasswordHaveBeenChangedPage])

    application.mountPageNoVersioning("find/people/${" + FindPeoplePage.QUERY_PARAM + "}", classOf[FindPeoplePage])

    application.mountPageNoVersioning("contacts", classOf[ContactsPage])
    application.mountPageNoVersioning("invitations/sent", classOf[InvitationsSentPage])
    application.mountPageNoVersioning("invitations/received", classOf[InvitationsReceivedPage])

    application.mountPageNoVersioning("conversations", classOf[UserConversationsPage])
    application.mountPageNoVersioning("conversation/${" + ConversationPage.CONVERSATION_ID_PARAM + "}", classOf[ConversationPage])
    application.mountPageNoVersioning("start/conversation/${" + StartConversationPage.USER_ID_PARAM + "}", classOf[StartConversationPage])

    application.mountPageNoVersioning("technologies", classOf[UserTechnologiesPage])

    application.mountPageNoVersioning("books/${" + BooksPage.QUERY_PARAM + "}", classOf[BooksPage])
    application.mountPageNoVersioning("suggest/book", classOf[SuggestBookPage])
    application.mountPageNoVersioning("book/suggestion/${" + BookSuggestionPreviewPage.BOOK_SUGGESTION_ID_PARAM + "}", classOf[BookSuggestionPreviewPage])
    application.mountPageNoVersioning("books/suggestions", classOf[BooksSuggestionsListPage])
    application.mountPageNoVersioning("your/books/suggestions", classOf[YourBooksSuggestionsPage])
    application.mountPageNoVersioning("add/book/${"+AddBookPage.BOOK_ID_PARAM+"}", classOf[AddBookPage])
    application.mountPageNoVersioning("book/${" + BookPreviewPage.BOOK_ID_PARAM + "}", classOf[BookPreviewPage])
    application.mountPageNoVersioning("your/books", classOf[YourBooksPage])
  }
}
