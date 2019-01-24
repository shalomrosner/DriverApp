package com.example.shalom.driverapp.model.datasource;

public interface NotifyDataChange<T> {
    void OnDataChanged(T obj);

    void onFailure(Exception exception);
}
