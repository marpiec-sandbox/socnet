package pl.marpiec.di

import pl.marpiec.cqrs.{DataStoreImpl, EventStoreImpl}
import pl.marpiec.socnet.database.UserDatabaseMockImpl
import pl.marpiec.socnet.service.user.{UserQueryImpl, UserCommandImpl}

class DefaultFactory {
  val eventStore: EventStoreImpl = new EventStoreImpl
  val dataStore: DataStoreImpl = new DataStoreImpl(eventStore)

  val userDatabase:UserDatabaseMockImpl = new UserDatabaseMockImpl

  val userCommand: UserCommandImpl = new UserCommandImpl(eventStore, dataStore, userDatabase)
  val userQuery:UserQueryImpl = new UserQueryImpl(userDatabase, dataStore)
}
