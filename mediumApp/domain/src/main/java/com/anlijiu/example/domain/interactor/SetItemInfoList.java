package com.anlijiu.example.domain.interactor;

import com.anlijiu.example.domain.executor.PostExecutionThread;
import com.anlijiu.example.domain.executor.ThreadExecutor;
import com.anlijiu.example.domain.objects.ItemInfo;
import com.anlijiu.example.domain.repository.ItemInfoRepository;

import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by anlijiu on 18-3-27.
 */
public class SetItemInfoList extends UseCase<Void, SetItemInfoList.Params> {
    private final ItemInfoRepository repository;

    @Inject
    SetItemInfoList(ItemInfoRepository repository, ThreadExecutor threadExecutor,
                    PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    Observable<Void> buildUseCaseObservable(Params params) {
        return repository.insertItemInfos(params.itemInfos);
    }


    public static final class Params {
        private final Collection<ItemInfo> itemInfos;

        private Params(Collection<ItemInfo> itemInfos) {
            this.itemInfos = itemInfos;
        }

        public static Params forItemInfoList(Collection<ItemInfo> itemInfos) {
            return new Params(itemInfos);
        }
    }

}
