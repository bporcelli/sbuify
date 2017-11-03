import { User } from "./user";

export class Customer extends User {
  public type: string = "customer"; // required for proper deserialization

  constructor(
    public email: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public birthday?: Date
  ) {
    super(email, password);
  }
}
