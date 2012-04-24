package pl.marpiec.socnet.di

import pl.marpiec.socnet.service.article.{ArticleCommandImpl, ArticleCommand}
import pl.marpiec.cqrs._
import pl.marpiec.socnet.service.user.{UserQuery, UserCommand, UserQueryImpl, UserCommandImpl}
import pl.marpiec.socnet.database._
import pl.marpiec.socnet.service.userprofile.{UserProfileCommandImpl, UserProfileCommand}

class DefaultFactory {
  val uidGenerator: UidGenerator = new UidGeneratorDbImpl

  val eventStore: EventStore = new EventStoreDbImpl
  val entityCache: AggregateCache = new AggregateCacheSimpleImpl

  val dataStore: DataStore = new DataStoreImpl(eventStore, entityCache)

  val userDatabase:UserDatabase = new UserDatabaseMockImpl(dataStore)
  val articleDatabase:ArticleDatabase = new ArticleDatabaseMockImpl(dataStore)
  val userProfileDatabase:UserProfileDatabase = new UserProfileDatabaseMockImpl(dataStore)

  val userCommand: UserCommand = new UserCommandImpl(eventStore, dataStore, userDatabase, uidGenerator)
  val userQuery:UserQuery = new UserQueryImpl(userDatabase, dataStore)

  val articleCommand:ArticleCommand = new ArticleCommandImpl(eventStore, dataStore, uidGenerator)

  val userProfileCommand:UserProfileCommand = new UserProfileCommandImpl(eventStore, dataStore, uidGenerator)
}
