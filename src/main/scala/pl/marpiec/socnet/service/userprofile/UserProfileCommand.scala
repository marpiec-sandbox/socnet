package pl.marpiec.socnet.service.userprofile

import input.PersonalSummary

/**
 * @author Marcin Pieciukiewicz
 */

trait UserProfileCommand {
  def createUserProfile(userId:Int):Int
  def updatePersonalSummary(id: Int, version: Int, personalSummary: PersonalSummary)
}
