package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.ProgrammerProfile

trait ProgrammerProfileDatabase {
  def getProgrammerProfileByUserId(userId: UID):Option[ProgrammerProfile]
}
