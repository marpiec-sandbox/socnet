package pl.marpiec.cqrs.exception

class ConcurrentAggregateModificationException(message:String) extends Exception(message)