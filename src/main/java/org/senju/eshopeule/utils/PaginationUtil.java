package org.senju.eshopeule.utils;

import org.senju.eshopeule.exceptions.PagingException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.util.*;

public class PaginationUtil {

    public static Pageable findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        checkValidSortFieldAndSortDirection(sortField, sortDirection);
        Sort sortSpec = sortDirection.equalsIgnoreCase(Sort.DEFAULT_DIRECTION.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return PageRequest.of(pageNo - 1, pageSize, sortSpec);
    }

    public static Pageable findPaginated(int pageNo, int pageSize) {
        return PageRequest.of(pageNo - 1, pageSize);
    }

    public static Pageable findPaginated(int pageNo, int pageSize, Map<String, String> sort) {
        final List<Sort.Order> orders = new ArrayList<>();
        sort.forEach(
                (sortField, sortDirection) -> {
                    checkValidSortFieldAndSortDirection(sortField, sortDirection);
                    final Sort.Order orderEle = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                            ? Sort.Order.asc(sortField)
                            : Sort.Order.desc(sortField);
                    orders.add(orderEle);
                }
        );
        return PageRequest.of(pageNo - 1, pageSize, Sort.by(orders));
    }

    public static Pageable findPaginated(int pageNo, int pageSize, String[] sortFields, String[] sortDirections) {
        if (sortFields == null || sortDirections == null || sortFields.length != sortDirections.length) {
            return PageRequest.of(pageNo - 1, pageSize);
        }

        final List<Sort.Order> orders = new ArrayList<>();
        for (int i = 0; i < sortFields.length; i++) {
            checkValidSortFieldAndSortDirection(sortFields[i], sortDirections[i]);
            final Sort.Order order = sortDirections[i].equalsIgnoreCase(Sort.DEFAULT_DIRECTION.name())
                    ? Sort.Order.asc(sortFields[i])
                    : Sort.Order.desc(sortFields[i]);
            orders.add(order);
        }
        return PageRequest.of(pageNo - 1, pageSize, Sort.by(orders));
    }

    public static Map<String, Object> buildSQLPagination(Pageable pageRequest, Map<String, String> sortProperties) {
        Map<String, Object> pagingProperties = new HashMap<>();
        if (pageRequest.getPageNumber() < 0) throw new PagingException("PageNo must be greater than or equal to 0");
        if (pageRequest.getPageSize() < 1) throw new PagingException("PageSize must be greater than or equal to 1");
        pagingProperties.put("pageNo", pageRequest.getPageNumber());
        pagingProperties.put("pageSize", pageRequest.getPageSize());

        if (pageRequest.getSort().isUnsorted()) return pagingProperties;
        final List<String> orderList = new ArrayList<>();
        pageRequest.getSort().forEach(
                order -> {
                    if (sortProperties.containsKey(order.getProperty())) {
                        orderList.add(String.join(
                                " ",
                                sortProperties.get(order.getProperty()),
                                order.getDirection().name())
                        );
                    }
                }
        );
        pagingProperties.put("orderByClause", orderList.isEmpty() ? null : String.join(", ", orderList));
        return pagingProperties;
    }

    private static void checkValidSortFieldAndSortDirection(String sortField, String sortDirection) {
        Assert.hasText(sortField, "Sort field must not be null or blank");
        Assert.hasText(sortDirection, "Sort direction must not be null or blank");
        if (!sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) && !sortDirection.equalsIgnoreCase(Sort.Direction.DESC.name())) {
            throw new PagingException("Sort direction is invalid");
        }
    }

}
