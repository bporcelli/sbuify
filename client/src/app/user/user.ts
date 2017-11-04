export class User {
  constructor(
    public email: String,
    public password: String,
    public type: String  // required for proper deserialization
  ) {}
}
