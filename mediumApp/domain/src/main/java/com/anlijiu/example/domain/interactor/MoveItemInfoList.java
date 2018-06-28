package com.anlijiu.example.domain.interactor;

import com.anlijiu.example.domain.executor.PostExecutionThread;
import com.anlijiu.example.domain.executor.ThreadExecutor;
import com.anlijiu.example.domain.objects.ItemSortData;
import com.anlijiu.example.domain.repository.ItemInfoRepository;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class MoveItemInfoList extends UseCase<Void, MoveItemInfoList.Params> {
    private final ItemInfoRepository repository;

    @Inject
    MoveItemInfoList(ItemInfoRepository repository, ThreadExecutor threadExecutor,
                     PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    Observable<Void> buildUseCaseObservable(Params params) {
        return repository.moveItemInfo(params.sortDatas);
    }


    public static final class Params {
        private final Collection<ItemSortData> sortDatas;

        private Params(Collection<ItemSortData> sortDatas) {
            this.sortDatas = sortDatas;
        }

        public static Params forSortDatas(Collection<ItemSortData> sortDatas) {
            return new Params(sortDatas);
        }
    }

}
