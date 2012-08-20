package pl.marpiec.socnet.web.page.books.bookPreviewPage.model

import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Pieciukiewicz
 */

object EditReviewFormModelValidator {
  
  val MINIMUM_REVIEW_LENGTH = 30

  def validate(reviewModel:EditReviewFormModel):Boolean = {
    
    if (StringUtils.length(reviewModel.reviewText) < MINIMUM_REVIEW_LENGTH) {
      return false;
    } 
    
    if(reviewModel.rating == null) {
      return false;
    }

    true;
  
  }
  
}
