package com.anlijiu.example.domain.interactor;

import com.anlijiu.example.domain.executor.PostExecutionThread;
import com.anlijiu.example.domain.executor.ThreadExecutor;
import com.anlijiu.example.domain.repository.ItemInfoRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by anlijiu on 18-3-27.
 */
public class RemoveInvalidItemInfos extends UseCase<Void, RemoveInvalidItemInfos.Params> {
    private final ItemInfoRepository repository;

    @Inject
    RemoveInvalidItemInfos(ItemInfoRepository repository, ThreadExecutor threadExecutor,
                    PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    Observable<Void> buildUseCaseObservable(RemoveInvalidItemInfos.Params params) {
        return repository.removeItemInfos(params.ids);
    }

    public static final class Params {
        private final long[] ids;

        private Params(long[] ids) {
            this.ids = ids;
        }

        public static Params forIds(long...ids) {
            return new Params(ids);
        }
    }

}
