import { User } from "../user/user";

export class Admin extends User {
  constructor(
    public email: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public superAdmin: boolean
  ) {
    super(email, password, 'admin');
  }
}
