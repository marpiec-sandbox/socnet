package pl.marpiec.socnet.web.page.editUserProfilePage.model

import pl.marpiec.util.ValidationResult
import org.apache.commons.lang.StringUtils
import pl.marpiec.util.validation.WwwValidator

/**
 * @author Marcin Pieciukiewicz
 */

object PersonalSummaryFormModelValidator {

  

  def validate(form: PersonalSummaryFormModel): ValidationResult = {
    val result = new ValidationResult()
    
    validateWwwPage(result, form)
    validateBlogPage(result, form)
    
    result
  }

  def validateWwwPage(result: ValidationResult, model: PersonalSummaryFormModel) {
    if (StringUtils.isNotBlank(model.wwwPage) && WwwValidator.isNotValid(model.wwwPage) ) {
      result.addError("Www page is incorrect")
    }
  }

  def validateBlogPage(result: ValidationResult, model: PersonalSummaryFormModel) {
    if (StringUtils.isNotBlank(model.blogPage) && WwwValidator.isNotValid(model.blogPage) ) {
      result.addError("Blog page is incorrect")
    }
  }
  
}
