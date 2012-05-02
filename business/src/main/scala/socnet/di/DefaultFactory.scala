package pl.marpiec.socnet.di

import pl.marpiec.socnet.service.article.{ArticleCommandImpl, ArticleCommand}
import pl.marpiec.cqrs._
import pl.marpiec.socnet.service.user.{UserQuery, UserCommand, UserQueryImpl, UserCommandImpl}
import pl.marpiec.socnet.database._
import pl.marpiec.socnet.service.userprofile.{UserProfileCommandImpl, UserProfileCommand}
import pl.marpiec.mailsender.{MailSenderImpl, MailSender}
import socnet.template.{TemplateRepositoryImpl, TemplateRepository}

class DefaultFactory {
  val connectionPool: DatabaseConnectionPool = new DatabaseConnectionPoolImpl

  val mailSender:MailSender = new MailSenderImpl
  
  val templateRepository:TemplateRepository = new TemplateRepositoryImpl

  val uidGenerator: UidGenerator = new UidGeneratorDbImpl(connectionPool)

  val eventStore: EventStore = new EventStoreDbImpl(connectionPool)
  val entityCache: AggregateCache = new AggregateCacheSimpleImpl

  val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)

  val userDatabase:UserDatabase = new UserDatabaseMockImpl(dataStore)
  val articleDatabase:ArticleDatabase = new ArticleDatabaseMockImpl(dataStore)
  val userProfileDatabase:UserProfileDatabase = new UserProfileDatabaseMockImpl(dataStore)

  val triggeredEvents:TriggeredEvents = new TriggeredEventsDatabaseImpl(connectionPool)
  
  val userCommand: UserCommand = new UserCommandImpl(eventStore, dataStore, triggeredEvents, userDatabase, uidGenerator, mailSender, templateRepository)
  val userQuery:UserQuery = new UserQueryImpl(userDatabase, dataStore)

  val articleCommand:ArticleCommand = new ArticleCommandImpl(eventStore, dataStore, uidGenerator)

  val userProfileCommand:UserProfileCommand = new UserProfileCommandImpl(eventStore, dataStore, uidGenerator)
}
