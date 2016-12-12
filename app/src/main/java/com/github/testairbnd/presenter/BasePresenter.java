package com.github.testairbnd.presenter;

import android.support.annotation.NonNull;

public interface BasePresenter<T> {

	void setView(@NonNull T view);

	void start();

	void loadModels(boolean showLoadingUI);

	void deleteAllModels();

}
