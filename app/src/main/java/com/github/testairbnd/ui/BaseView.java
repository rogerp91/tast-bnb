package com.github.testairbnd.ui;

public interface BaseView<T> {

    void setTitle();

    void showProgress(boolean active);

    void setLoadingIndicator(boolean active);

    void showModels(T objects);

    void showNoModels(final boolean active);

    void showNetworkError(final boolean active);

    void showErrorOcurred(final boolean active);

    void showErrorNotSolve(final boolean active);

    void showMessage(String message);

    boolean isActive();
}
