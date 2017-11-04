import { User } from "./user";

export class Customer extends User {
  constructor(
    public email: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public birthday?: Date
  ) {
    super(email, password, "customer");
  }
}
