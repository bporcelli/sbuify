import { User } from "./user";

export class Customer extends User {
  public premium: boolean;

  constructor(
    public email: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public birthday?: Date
  ) {
    super(email, password, "customer");
  }

  get name(): string {
    return this.firstName + this.lastName;
  }
}
