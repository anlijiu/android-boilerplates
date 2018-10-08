package com.example.mvp;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import java.lang.ref.WeakReference;


public abstract class MvpPresenter<V extends IMvpView, VM extends ViewModel> implements IMvpPresenter<V, VM> {

    /**
     * An Action that is executed to interact with the view.
     * Usually a Presenter should not get any data from View: so calls like view.getUserId() should not be done.
     * Rather write a method in your Presenter that takes the user id as parameter like this:
     * {@code
     * void doSomething(int userId){
     * // do something
     * ...
     * <p>
     * ifViewAttached( view -> view.showSuccessful())
     * }
     *
     * @param <V> The Type of the View
     */
    public interface ViewAction<V> {

        /**
         * This method will be invoked to run the action. Implement this method to interact with the view.
         *
         * @param view The reference to the view. Not null.
         */
        void run(@NonNull V view);
    }

    public interface ViewModelAction<VM> {
        void run(@NonNull VM viewModel);
    }

    private WeakReference<V> viewRef;
    private WeakReference<VM> viewModelRef;
    private boolean presenterDestroyed = false;

    @UiThread
    @Override
    final public void attachView(V view) {
        viewRef = new WeakReference<V>(view);
        presenterDestroyed = false;
        onAttachView(view);
    }

    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    public VM getViewModel() {
        return viewModelRef == null ? null : viewModelRef.get();
    }

    @UiThread
    @Override
    final public void attachViewModel(VM viewModel) {
        viewModelRef = new WeakReference<>(viewModel);
        presenterDestroyed = false;
        onAttachViewModel(viewModel);
    }

    /**
     * Executes the passed Action only if the View is attached.
     * If no View is attached, either an exception is thrown (if parameter exceptionIfViewNotAttached
     * is true) or the action is just not executed (no exception thrown).
     * Note that if no view is attached, this will not re-execute the given action if the View gets
     * re-attached.
     *
     * @param exceptionIfViewNotAttached true, if an exception should be thrown if no view is
     *                                   attached while trying to execute the action. false, if no exception should be thrown (the action
     *                                   will not be executed since no view is attached)
     * @param action                     The {@link ViewAction} that will be executed if a view is attached. This is
     *                                   where you call view.isLoading etc. Use the view reference passed as parameter to {@link
     *                                   ViewAction#run(Object)}
     */
    protected final void ifViewAttached(boolean exceptionIfViewNotAttached, ViewAction<V> action) {
        final V view = viewRef == null ? null : viewRef.get();
        if (view != null) {
            action.run(view);
        } else if (exceptionIfViewNotAttached) {
            throw new IllegalStateException(
                    "No View attached to Presenter. Presenter destroyed = " + presenterDestroyed);
        }
    }

    /**
     * Calls {@link #ifViewAttached(boolean, ViewAction)} with false as first parameter (don't throw
     * exception if view not attached).
     *
     * @see #ifViewAttached(ViewAction)
     */
    protected final void ifViewAttached(ViewAction<V> action) {
        ifViewAttached(false, action);
    }

    protected final void ifViewModelAttached(ViewModelAction<VM> action) {
        final VM viewModel = viewModelRef == null ? null : viewModelRef.get();
        if(viewModel != null) {
            action.run(viewModel);
        }
    }
 
    /**
     * {@inheritDoc}
     */
    @Override
    final public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }

        onDetachView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public void detachViewModel() {
        if (viewModelRef != null) {
            viewModelRef.clear();
            viewModelRef = null;
        }

        onDetachView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        detachView();
        detachViewModel();
        presenterDestroyed = true;
    }
}
