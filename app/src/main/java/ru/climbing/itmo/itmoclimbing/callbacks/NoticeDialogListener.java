package ru.climbing.itmo.itmoclimbing.callbacks;

/**
 * Created by Игорь on 17.12.2016.
 */

public interface NoticeDialogListener<T> {
    void onDialogPositiveClick(T data);
    void onDialogNegativeClick();
}
