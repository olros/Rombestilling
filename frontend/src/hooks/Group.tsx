import { useMutation, useInfiniteQuery, useQuery, useQueryClient, UseMutationResult } from 'react-query';
import API from 'api/api';
import { Group, GroupCreate, PaginationResponse, RequestResponse } from 'types/Types';
import { getNextPaginationPage } from 'utils';

export const GROUP_QUERY_KEY = 'group';
export const GROUPS_QUERY_KEY = 'groups';

export const useGroup = (groupId: string) => {
  return useQuery<Group, RequestResponse>([GROUP_QUERY_KEY, groupId], () => API.getGroup(groupId));
};

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const useGroups = (filters?: any) => {
  return useInfiniteQuery<PaginationResponse<Group>, RequestResponse>(
    [GROUPS_QUERY_KEY, filters],
    ({ pageParam = 0 }) => API.getGroups({ sort: 'name,ASC', ...filters, page: pageParam }),
    {
      getNextPageParam: getNextPaginationPage,
    },
  );
};

export const useCreateGroup = (): UseMutationResult<Group, RequestResponse, GroupCreate, unknown> => {
  const queryClient = useQueryClient();
  return useMutation((group) => API.createGroup(group), {
    onSuccess: () => {
      queryClient.invalidateQueries(GROUPS_QUERY_KEY);
    },
  });
};

export const useUpdateGroup = (): UseMutationResult<Group, RequestResponse, { groupId: string; group: Partial<Group> }, unknown> => {
  const queryClient = useQueryClient();
  return useMutation(({ groupId, group }) => API.updateGroup(groupId, group), {
    onSuccess: (group) => {
      queryClient.invalidateQueries([GROUP_QUERY_KEY, undefined]);
      queryClient.invalidateQueries([GROUP_QUERY_KEY, group.id]);
      queryClient.invalidateQueries(GROUPS_QUERY_KEY);
    },
  });
};

export const useDeleteGroup = (): UseMutationResult<RequestResponse, RequestResponse, string, unknown> => {
  return useMutation((groupId) => API.deleteGroup(groupId));
};
