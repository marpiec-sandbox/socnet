package pl.marpiec.socnet.di

import pl.marpiec.socnet.service.article.{ArticleCommandImpl, ArticleCommand}
import pl.marpiec.cqrs._
import pl.marpiec.socnet.service.user.{UserQuery, UserCommand, UserQueryImpl, UserCommandImpl}
import pl.marpiec.socnet.database._
import pl.marpiec.socnet.service.userprofile.{UserProfileCommandImpl, UserProfileCommand}

class DefaultFactory {
  val eventStore: EventStore = new EventStoreDbImpl
  val entityCache: EntityCache = new EntityCacheSimpleImpl

  val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)

  val userDatabase:UserDatabase = new UserDatabaseMockImpl(dataStore)
  val articleDatabase:ArticleDatabase = new ArticleDatabaseMockImpl(dataStore)
  val userProfileDatabase:UserProfileDatabase = new UserProfileDatabaseMockImpl(dataStore)

  val userCommand: UserCommand = new UserCommandImpl(eventStore, dataStore, userDatabase)
  val userQuery:UserQuery = new UserQueryImpl(userDatabase, dataStore)

  val articleCommand:ArticleCommand = new ArticleCommandImpl(eventStore, dataStore)

  val userProfileCommand:UserProfileCommand = new UserProfileCommandImpl(eventStore, dataStore)
}
