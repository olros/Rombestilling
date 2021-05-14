import { useMutation, useInfiniteQuery, useQuery, useQueryClient, UseMutationResult } from 'react-query';
import API from 'api/api';
import URLS from 'URLS';
import { User, UserList, UserCreate, PaginationResponse, LoginRequestResponse, RequestResponse, RefreshTokenResponse } from 'types/Types';
import { getCookie, setCookie, removeCookie } from 'api/cookie';
import { ACCESS_TOKEN, REFRESH_TOKEN, ACCESS_TOKEN_DURATION, REFRESH_TOKEN_DURATION } from 'constant';
import { getNextPaginationPage } from 'utils';

export const USER_QUERY_KEY = 'user';
export const USERS_QUERY_KEY = 'users';

export const useUser = (userId?: string) => {
  const isAuthenticated = useIsAuthenticated();
  return useQuery<User | undefined, RequestResponse>([USER_QUERY_KEY, userId], () => (isAuthenticated ? API.getUser(userId) : undefined));
};

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const useUsers = (filters?: any) => {
  return useInfiniteQuery<PaginationResponse<UserList>, RequestResponse>(
    [USERS_QUERY_KEY, filters],
    ({ pageParam = 0 }) => API.getUsers({ sort: 'firstName,surname,ASC', ...filters, page: pageParam }),
    {
      getNextPageParam: getNextPaginationPage,
    },
  );
};

export const useRefreshUser = () => {
  const queryClient = useQueryClient();
  return () => {
    queryClient.invalidateQueries(USER_QUERY_KEY);
  };
};

export const useLogin = (): UseMutationResult<LoginRequestResponse, RequestResponse, { email: string; password: string }, unknown> => {
  const queryClient = useQueryClient();
  return useMutation(({ email, password }) => API.authenticate(email, password), {
    onSuccess: (data) => {
      setCookie(ACCESS_TOKEN, data.token, ACCESS_TOKEN_DURATION);
      setCookie(REFRESH_TOKEN, data.refreshToken, REFRESH_TOKEN_DURATION);
      queryClient.removeQueries(USER_QUERY_KEY);
      queryClient.prefetchQuery(USER_QUERY_KEY, () => API.getUser());
    },
  });
};

export const useRefreshToken = (): UseMutationResult<RefreshTokenResponse, RequestResponse, unknown, unknown> => {
  return useMutation(() => API.refreshAccessToken());
};

export const useForgotPassword = (): UseMutationResult<RequestResponse, RequestResponse, string, unknown> => {
  return useMutation((email) => API.forgotPassword(email));
};

export const useResetPassword = (): UseMutationResult<RequestResponse, RequestResponse, { email: string; token: string; password: string }, unknown> => {
  return useMutation(({ email, token, password }) => API.resetPassword(email, password, token));
};

export const useLogout = () => {
  const queryClient = useQueryClient();
  return () => {
    queryClient.removeQueries(USER_QUERY_KEY);
    logout();
  };
};

export const logout = () => {
  removeCookie(ACCESS_TOKEN);
  removeCookie(REFRESH_TOKEN);
  location.href = URLS.LANDING;
};

export const useIsAuthenticated = () => {
  return typeof getCookie(REFRESH_TOKEN) !== 'undefined';
};

export const useBatchCreateUser = (): UseMutationResult<RequestResponse, RequestResponse, File, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((file) => API.batchAddUser(file), {
    onSuccess: () => {
      queryClient.invalidateQueries(USERS_QUERY_KEY);
    },
  });
};

export const useCreateUser = (): UseMutationResult<RequestResponse, RequestResponse, UserCreate, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((user) => API.createUser(user), {
    onSuccess: () => {
      queryClient.invalidateQueries(USERS_QUERY_KEY);
    },
  });
};

export const useUpdateUser = (): UseMutationResult<User, RequestResponse, { userId: string; user: Partial<User> }, unknown> => {
  const queryClient = useQueryClient();
  return useMutation(({ userId, user }) => API.updateUser(userId, user), {
    onSuccess: () => {
      queryClient.invalidateQueries([USER_QUERY_KEY, undefined]);
      queryClient.invalidateQueries(USERS_QUERY_KEY);
    },
  });
};

export const useChangePassword = (): UseMutationResult<RequestResponse, RequestResponse, { oldPassword: string; newPassword: string }, unknown> => {
  return useMutation(({ oldPassword, newPassword }) => API.changePassword(oldPassword, newPassword));
};

export const useDeleteUser = (): UseMutationResult<RequestResponse, RequestResponse, unknown, unknown> => {
  return useMutation(() => API.deleteUser());
};
