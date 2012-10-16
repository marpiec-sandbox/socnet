package pl.marpiec.socnet.web.page.books.suggestBookPage.model

import pl.marpiec.util.ValidationResult
import pl.marpiec.util.validation.StringValidator

/**
 * @author Marcin Pieciukiewicz
 */

object SuggestBookFormModelValidator {
  def validate(model: SuggestBookFormModel): ValidationResult = {
    val result = new ValidationResult()

    StringValidator.validate(result, model.title, 3, 200, "Title is required", "Title has to be at least 3 chars, and maximum 200 chars")
    StringValidator.validate(result, model.description, 100, 10000, "Description is required", "Description has to be at least 100 chars")
    StringValidator.validate(result, model.isbn, 10, 20, "ISBN is required", "ISBN has to be at least 10 chars")

    result
  }
}
