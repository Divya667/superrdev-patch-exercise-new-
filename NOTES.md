## Bugs Reviewed and Fixes

1. **SQL search filtering:** Grouped title and description conditions so archived and status filters apply consistently.
2. **Oracle SQL filtering:** Applied the same condition grouping to both count and result queries.
3. **Pagination validation:** Validate page number and page size; return HTTP 400 for invalid values.
4. **Artificial delay:** Remove unnecessary `Thread.sleep()` logic from the request path.
5. **Pagination reset:** Reset the page to 1 when the search query or status changes.
6. **Loading and error handling:** Clear previous errors when a request starts and reset loading in `finally`.
7. **Stale search responses:** Ignore or cancel obsolete requests so older results cannot overwrite newer ones.
8. **Status validation:** Handle unsupported status values and return HTTP 400.
9. **Database pagination:** Use Spring Data `Pageable` and `Page` to fetch only the requested page.
10. **Debounced search:** Debounce search input to reduce unnecessary API requests.

## Verification

Test SQL filters, invalid pagination values, invalid status values, failed API requests, rapid searches, and pagination behavior. Confirm performance improvements using request timing and database query logs.

**Note:** These are intended fixes from the code review. Mark a bug as fixed only after applying the change and testing it. Performance items such as artificial delay, database pagination, and debouncing should be prioritized according to the assignment's scope.
