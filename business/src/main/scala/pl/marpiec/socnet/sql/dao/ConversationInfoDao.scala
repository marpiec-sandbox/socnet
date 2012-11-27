package pl.marpiec.socnet.sql.dao

import pl.marpiec.socnet.sql.entity.ConversationInfo
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait ConversationInfoDao {

  def read(userId:UID, conversationId:UID):Option[ConversationInfo]
  def readOrCreate(userId:UID, conversationId:UID):ConversationInfo
  def update(conversationInfo: ConversationInfo)
  
}
