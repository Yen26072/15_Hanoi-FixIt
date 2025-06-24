package com.example.hanoi_fixlt.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> dataChanged = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> reportStatusChanged = new MutableLiveData<>();

    public LiveData<Boolean> getDataChanged() {
        return dataChanged;
    }

    public void notifyDataChanged() {
        dataChanged.setValue(true);
    }

    public LiveData<Boolean> getReportStatusChanged() {
        return reportStatusChanged;
    }
    public void notifyDataChanged2() {
        reportStatusChanged.setValue(true);
    }
}