/* eslint-disable @typescript-eslint/no-explicit-any */
import { IFetch } from 'api/fetch';
import { setCookie } from 'api/cookie';
import { ACCESS_TOKEN, ACCESS_TOKEN_DURATION, REFRESH_TOKEN, REFRESH_TOKEN_DURATION } from 'constant';
import { logout } from 'hooks/User';
import {
  FileUploadResponse,
  LoginRequestResponse,
  PaginationResponse,
  RequestResponse,
  RefreshTokenResponse,
  User,
  UserCreate,
  UserList,
  SectionCreate,
  SectionList,
  Section,
} from 'types/Types';

export const USERS = 'users';
export const ME = 'me';
export const AUTH = 'auth';
export const SECTIONS = 'sections';

export default {
  // Auth
  createUser: (item: UserCreate) => IFetch<RequestResponse>({ method: 'POST', url: `${USERS}/`, data: item, withAuth: false, tryAgain: false }),
  authenticate: (email: string, password: string) =>
    IFetch<LoginRequestResponse>({
      method: 'POST',
      url: `${AUTH}/login`,
      data: { email, password },
      withAuth: false,
      tryAgain: false,
    }),
  forgotPassword: (email: string) =>
    IFetch<RequestResponse>({ method: 'POST', url: `${AUTH}/forgot-password/`, data: { email }, withAuth: false, tryAgain: false }),
  resetPassword: (email: string, newPassword: string, token: string) =>
    IFetch<RequestResponse>({ method: 'POST', url: `${AUTH}/reset-password/${token}/`, data: { email, newPassword }, withAuth: false, tryAgain: false }),
  refreshAccessToken: () =>
    IFetch<RefreshTokenResponse>({ method: 'GET', url: `${AUTH}/refresh-token/`, refreshAccess: true, withAuth: false })
      .then((tokens) => {
        setCookie(ACCESS_TOKEN, tokens.token, ACCESS_TOKEN_DURATION);
        setCookie(REFRESH_TOKEN, tokens.refreshToken, REFRESH_TOKEN_DURATION);
        return tokens;
      })
      .catch((e) => {
        logout();
        throw e;
      }),
  changePassword: (oldPassword: string, newPassword: string) =>
    IFetch<RequestResponse>({ method: 'POST', url: `${AUTH}/change-password/`, data: { oldPassword, newPassword } }),
  deleteUser: () => IFetch<RequestResponse>({ method: 'DELETE', url: `${USERS}/me/` }),

  // Section
  getSection: (sectionId: string) => IFetch<Section>({ method: 'GET', url: `${SECTIONS}/${sectionId}/` }),
  getSections: (filters?: any) => IFetch<PaginationResponse<SectionList>>({ method: 'GET', url: `${SECTIONS}/`, data: filters || {} }),
  createSection: (newPost: SectionCreate) => IFetch<Section>({ method: 'POST', url: `${SECTIONS}/`, data: newPost }),
  updateSection: (sectionId: string, updatedSection: Partial<Section>) =>
    IFetch<Section>({ method: 'PUT', url: `${SECTIONS}/${sectionId}/`, data: updatedSection }),
  deleteSection: (sectionId: string) => IFetch<RequestResponse>({ method: 'DELETE', url: `${SECTIONS}/${sectionId}/` }),
  // User
  getUser: (userId?: string) => IFetch<User>({ method: 'GET', url: `${USERS}/${userId || ME}/` }),
  getUsers: (filters?: any) => IFetch<PaginationResponse<UserList>>({ method: 'GET', url: `${USERS}/`, data: filters || {} }),
  updateUser: (userId: string, item: Partial<User>) => IFetch<User>({ method: 'PUT', url: `${USERS}/${userId}/`, data: item }),

  // Upload file
  uploadFile: (file: File | Blob) =>
    IFetch<FileUploadResponse>({ method: 'POST', url: 'https://api.imgbb.com/1/upload?key=909df01fa93bd63405c9a36d662523f3', file, withAuth: false }),
};
