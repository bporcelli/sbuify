export class User {
  public id: number;

  constructor(
    public email: string,
    public password: string,
    public type: string  // required for proper deserialization
  ) {}
}
