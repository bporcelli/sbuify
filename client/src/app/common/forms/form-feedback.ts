export class FormFeedback {
  type: String;
  text: String;

  constructor(text: String, type: String = "error") {
    this.type = type;
    this.text = text;
  }

  get class(): String {
    return "form-feedback " + this.type;
  }
}
