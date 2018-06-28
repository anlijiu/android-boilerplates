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
 * Created by anlijiu on 18-6-27.
 */
public class UpdateSimpleList extends UseCase<List<SimpleItem>, UpdateSimpleList.Params> {
    private final SimpleRepository repository;

    @Inject
    UpdateSimpleList(SimpleRepository repository, ThreadExecutor threadExecutor,
                    PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    Observable<List<SimpleItem>> buildUseCaseObservable(UpdateSimpleList.Params params) {
        return params.type == Params.Type.ADD ?  repository.addRandomItem() : repository.removeRandomItem();
    }

    public static final class Params {
        enum Type {
            ADD,
            REMOVE
        }
        private final Type type;

        private Params(Type type) {
            this.type = type;
        }

        public static Params add() {
            return new Params(Type.ADD);
        }

        public static Params remove() {
            return new Params(Type.REMOVE);
        }
    }

}
