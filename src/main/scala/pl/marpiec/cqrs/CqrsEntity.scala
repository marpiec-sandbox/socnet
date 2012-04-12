package pl.marpiec.cqrs

abstract class CqrsEntity(var id:Int, var version:Int) {
  def copy:CqrsEntity
}
