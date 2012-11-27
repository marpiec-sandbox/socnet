package pl.marpiec.socnet.sql.dao

import org.springframework.orm.hibernate3.support.HibernateDaoSupport
import pl.marpiec.socnet.sql.entity.ConversationInfo
import pl.marpiec.util.UID
import org.springframework.stereotype.Repository

/**
 * @author Marcin Pieciukiewicz
 */

//@Repository("conversationInfoDao")
class ConversationInfoDaoImpl extends HibernateDaoSupport with ConversationInfoDao {
  def read(userId: UID, conversationId: UID):Option[ConversationInfo] = {
    val example = new ConversationInfo
    example.userId = userId
    example.conversationId = conversationId
    
    val listResult = getHibernateTemplate.findByExample(example)
    if (listResult.size() == 1) {
      Option(listResult.get(0).asInstanceOf[ConversationInfo])
    } else if (listResult.isEmpty) {
      None
    } else {
      throw new IllegalStateException("Expected one ConversationInfo for user "+userId+" and conversation "+conversationId)
    }
  }

  def readOrCreate(userId: UID, conversationId: UID) = {
    val conversationOption = read(userId, conversationId)
    if(conversationOption.isDefined) {
      conversationOption.get
    } else {
      val conversation = new ConversationInfo
      conversation.userId = userId
      conversation.conversationId = conversationId
      getHibernateTemplate.save(conversation)
      conversation
    }
  }

  def update(conversationInfo: ConversationInfo) {
    getHibernateTemplate.save(conversationInfo)
  }
}
