package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.Article
//import com.mongodb.casbah.Imports._
//import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers


/**
 * @author Marcin Pieciukiewicz
 */

class ArticleDatabaseMongoDBImpl extends ArticleDatabase {
 /*
  RegisterJodaTimeConversionHelpers()

  val mongoConn = MongoConnection("localhost", 42017)

  val socnetDB = mongoConn("socnet")
  */
  def addArticle(article: Article) {
  //  val newObj = MongoDBObject()
  }

  def updateArticle(article: Article) {

  }

  def getArticleById(id: UID): Option[Article] = {
      null
  }

  def getAllArticles: List[Article] = {
       null
  }
}
