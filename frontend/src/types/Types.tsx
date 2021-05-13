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
};

export type UserList = Pick<User, 'id' | 'firstName' | 'surname' | 'email' | 'image'>;

export type UserCreate = Pick<User, 'email' | 'firstName' | 'surname' | 'phoneNumber'> & {
  password: string;
};

export type RoomBase = {
  id: string;
  name: string;
  capacity: number;
  description: string;
  image: string;
};

export type Room = RoomBase & {
  type: 'room';
  children: Array<Omit<SectionList, 'parent'>>;
};

export type RoomList = Omit<Room, 'description' | 'image'>;

export type Section = RoomBase & {
  type: 'section';
  parent: Omit<RoomList, 'children'>;
};

export type SectionList = Omit<Section, 'description' | 'image'>;

export type SectionCreate = Omit<RoomBase, 'id'> & {
  parentId?: string;
};

export type FileUploadResponse = {
  data: {
    display_url: string;
  };
};
