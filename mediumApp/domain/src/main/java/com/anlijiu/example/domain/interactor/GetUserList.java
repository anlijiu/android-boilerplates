package com.anlijiu.example.domain.interactor;

import com.anlijiu.example.domain.executor.PostExecutionThread;
import com.anlijiu.example.domain.executor.ThreadExecutor;
import com.anlijiu.example.domain.objects.ItemInfo;
import com.anlijiu.example.domain.objects.User;
import com.anlijiu.example.domain.repository.CloudRepository;
import com.anlijiu.example.domain.repository.ItemInfoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


public class GetUserList extends UseCase<List<User>, GetUserList.Params> {
    private final CloudRepository repository;

    @Inject
    GetUserList(CloudRepository repository, ThreadExecutor threadExecutor,
                    PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    Observable<List<User>> buildUseCaseObservable(GetUserList.Params params) {
        return repository.getUsers(params.lastId, params.perPage, params.shouldUpdate);
    }

    public static final class Params {
        public static final int PER_PAGE = 25;
        private final int lastId;
        private final int perPage;
        private final boolean shouldUpdate;

        private Params(int lastId, int perPage, boolean shouldUpdate) {
            this.lastId = lastId;
            this.perPage = perPage;
            this.shouldUpdate = shouldUpdate;
        }

        public static Params forLastId(int lastId) {
            return new Params(lastId, PER_PAGE, false);
        }

        public static Params forLastIdAndPerPage(int lastId, int perPage) {
            return new Params(lastId, perPage, false);
        }

        public static Params create(int lastId, int perPage, boolean shouldUpdate) {
            return new Params(lastId, perPage, false);
        }
    }

}
