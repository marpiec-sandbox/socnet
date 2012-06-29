package pl.marpiec.cqrs.exception

class ConcurrentAggregateModificationException(val message:String) extends Exception