package ua.comparus.userservice.repository.impl;

import org.springframework.stereotype.Repository;
import ua.comparus.userservice.model.UserDTO;
import ua.comparus.userservice.model.UserSearchParams;
import ua.comparus.userservice.repository.UserRepository;
import ua.comparus.userservice.repository.datasource.DataSourceRegistry;
import ua.comparus.userservice.repository.util.SqlQueryBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Repository
public class MultiSourceUserRepository implements UserRepository {
    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";

    private final DataSourceRegistry registry;
    private final ExecutorService virtualThreadExecutor;

    public MultiSourceUserRepository(DataSourceRegistry registry, ExecutorService virtualThreadExecutor) {
        this.registry = registry;
        this.virtualThreadExecutor = virtualThreadExecutor;
    }

    @Override
    public List<UserDTO> findAll(UserSearchParams params) {
        var futures = registry.getAllContexts().stream()
                .map(ctx -> CompletableFuture.supplyAsync(() -> queryContext(ctx, params), virtualThreadExecutor))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .toList();
    }

    private List<UserDTO> queryContext(DataSourceRegistry.DataSourceContext ctx, UserSearchParams params) {
        var queryBuilder = prepareQuery(ctx, params);
        return ctx.jdbcClient()
                .sql(queryBuilder.build())
                .params(queryBuilder.getParams())
                .query(UserDTO.class)
                .list();
    }

    private SqlQueryBuilder prepareQuery(DataSourceRegistry.DataSourceContext ctx, UserSearchParams params) {
        var mapping = ctx.config().mapping();
        return new SqlQueryBuilder()
                .select()
                .fieldAs(mapping.get(ID), ID)
                .fieldAs(mapping.get(USERNAME), USERNAME)
                .fieldAs(mapping.get(NAME), NAME)
                .fieldAs(mapping.get(SURNAME), SURNAME)
                .from(ctx.config().table())
                .where(mapping.get(USERNAME), params.username())
                .andLike(mapping.get(NAME), params.name())
                .andLike(mapping.get(SURNAME), params.surname());
    }
}