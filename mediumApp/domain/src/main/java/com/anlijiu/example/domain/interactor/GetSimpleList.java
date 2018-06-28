package com.anlijiu.example.domain.interactor;

import com.anlijiu.example.domain.executor.PostExecutionThread;
import com.anlijiu.example.domain.executor.ThreadExecutor;
import com.anlijiu.example.domain.objects.ItemInfo;
import com.anlijiu.example.domain.objects.SimpleItem;
import com.anlijiu.example.domain.repository.ItemInfoRepository;
import com.anlijiu.example.domain.repository.SimpleRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by anlijiu on 18-3-27.
 */
public class GetSimpleList extends UseCase<List<SimpleItem>, Void> {
    private final SimpleRepository repository;

    @Inject
    GetSimpleList(SimpleRepository repository, ThreadExecutor threadExecutor,
                     PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    Observable<List<SimpleItem>> buildUseCaseObservable(Void unused) {
        return repository.simpleItems();
    }
}
