package com.anlijiu.example.domain.interactor;

import com.anlijiu.example.domain.executor.PostExecutionThread;
import com.anlijiu.example.domain.executor.ThreadExecutor;
import com.anlijiu.example.domain.objects.ItemInfo;
import com.anlijiu.example.domain.repository.ItemInfoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by anlijiu on 18-3-27.
 */
public class GetItemInfoList extends UseCase<List<ItemInfo>, GetItemInfoList.Params> {
    private final ItemInfoRepository repository;

    @Inject
    GetItemInfoList(ItemInfoRepository repository, ThreadExecutor threadExecutor,
                    PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    Observable<List<ItemInfo>> buildUseCaseObservable(GetItemInfoList.Params params) {
        return repository.itemInfos(params.itemTypes);
    }

    public static final class Params {
        private final int[] itemTypes;

        private Params(int[] itemTypes) {
            this.itemTypes = itemTypes;
        }

        public static Params forItemTypes(int...itemTypes) {
            return new Params(itemTypes);
        }

        public static Params forItemType(int itemType) {
            return new Params(new int[]{itemType});
        }
    }

}
