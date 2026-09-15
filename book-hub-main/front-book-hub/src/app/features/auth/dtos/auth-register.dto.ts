export interface AuthRegisterDto {
  email: string;
  firstName: string;
  lastName: string;
  password: string;
  phone?: string | null;
  username: string;
}
