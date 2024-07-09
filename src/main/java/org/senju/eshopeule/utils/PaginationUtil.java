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
        final List<Sort.Order> orders = new ArrayList<>();
        if (sortFields.length != sortDirections.length) {
            Arrays.stream(sortFields).forEach(
                    sf -> {
                        Assert.hasText(sf, "Sort field must not be null or blank");
                        orders.add(Sort.Order.asc(sf));
                    }
            );
        } else {
            for (int i = 0; i < sortFields.length; i++) {
                checkValidSortFieldAndSortDirection(sortFields[i], sortDirections[i]);
                final Sort.Order order = sortDirections[i].equalsIgnoreCase(Sort.DEFAULT_DIRECTION.name())
                        ? Sort.Order.asc(sortFields[i])
                        : Sort.Order.desc(sortFields[i]);
                orders.add(order);
            }
        }
        return PageRequest.of(pageNo - 1, pageSize, Sort.by(orders));
    }

    private static void checkValidSortFieldAndSortDirection(String sortField, String sortDirection) {
        Assert.hasText(sortField, "Sort field must not be null or blank");
        Assert.hasText(sortDirection, "Sort direction must not be null or blank");
        if (!sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) && !sortDirection.equalsIgnoreCase(Sort.Direction.DESC.name())) {
            throw new PagingException("Sort direction is invalid");
        }
    }

}
