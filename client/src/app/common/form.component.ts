import { FormFeedback } from "./form-feedback";

/**
 * Base class extended by form components.
 */
export class FormComponent {

  feedback: FormFeedback = null;
  disabled: boolean = false;

  /**
   * Form submission handler.
   */
  onSubmit() {
    this.feedback = null;
    this.disabled = true;
  }

  /**
   * Show feedback to the user.
   *
   * @param message Feedback message.
   * @param type Feedback type ('error', or 'success').
   */
  showFeedback(message: string, type: string = "error") {
    this.feedback = new FormFeedback(message, type);
  }
}
