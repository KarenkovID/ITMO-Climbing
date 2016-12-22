package ru.climbing.itmo.itmoclimbing.loader;

/**
 * Три возможных результат процесса загрузки данных.
 */
public enum ResultType {

    /**
     * Данные успешно загружены.
     */
    OK,

    /**
     * Данные не загружены из-за отсутствия интернета,
     * возврашаем то, что в кэше
     */
    NO_INTERNET_LOADED_FROM_CACHE,
    /**
     * Данные не загружены по другой причине.
     */
    ERROR
}
