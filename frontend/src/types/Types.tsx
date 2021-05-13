import { UserRole } from './Enums';

export type RequestResponse = {
  message: string;
};

export type LoginRequestResponse = {
  token: string;
  refreshToken: string;
};

export type RefreshTokenResponse = {
  token: string;
  refreshToken: string;
};

export type PaginationResponse<T> = {
  totalElements: number;
  totalPages: number;
  number: number;
  content: Array<T>;
  empty: boolean;
  last: boolean;
};

export type User = {
  id: string;
  firstName: string;
  surname: string;
  email: string;
  phoneNumber: string;
  image: string;
  roles: Array<{
    name: UserRole;
  }>;
};

export type UserList = Pick<User, 'id' | 'firstName' | 'surname' | 'email' | 'image'>;

export type UserCreate = Pick<User, 'email' | 'firstName' | 'surname' | 'phoneNumber'> & {
  password: string;
};

export type Section = {
  id: string;
  name: string;
  capacity: number;
  description: string;
  image: string;
  type: 'room' | 'section';
  parent: SectionChild;
  children: Array<SectionChild>;
};

export type SectionChild = Omit<SectionList, 'parent' | 'children'>;

export type SectionList = Omit<Section, 'description' | 'image'>;

export type SectionCreate = Omit<Section, 'id'> & {
  parentId?: string;
};

export type Reservation = {
  id: string;
  section: SectionList;
  fromTime: string;
  toTime: string;
  text: string;
  nrOfPeople: number;
  user: UserList;
};

export type ReservationCreate = Pick<Reservation, 'nrOfPeople' | 'text' | 'fromTime' | 'toTime'> & {
  userId: string;
};

export type FileUploadResponse = {
  data: {
    display_url: string;
  };
};
