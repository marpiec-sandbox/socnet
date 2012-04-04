package pl.marpiec.di

import pl.marpiec.socnet.database.UserDatabaseMockImpl
import pl.marpiec.socnet.service.user.{UserQueryImpl, UserCommandImpl}
import pl.marpiec.cqrs.{EntityCacheSimpleImpl, EntityCache, DataStoreImpl, EventStoreImpl}

class DefaultFactory {
  val eventStore: EventStoreImpl = new EventStoreImpl
  val entityCache: EntityCache = new EntityCacheSimpleImpl

  val dataStore: DataStoreImpl = new DataStoreImpl(eventStore, entityCache)

  val userDatabase:UserDatabaseMockImpl = new UserDatabaseMockImpl

  val userCommand: UserCommandImpl = new UserCommandImpl(eventStore, dataStore, userDatabase)
  val userQuery:UserQueryImpl = new UserQueryImpl(userDatabase, dataStore)
}
